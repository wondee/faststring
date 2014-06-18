package de.unifrankfurt.faststring.analysis.graph;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ibm.wala.ssa.SSAInstruction;

import de.unifrankfurt.faststring.analysis.IRMethod;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;
import de.unifrankfurt.faststring.analysis.util.UniqueQueue;

public class DataFlowGraphBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(DataFlowGraphBuilder.class);
	
	private final TypeLabel label;
	private final IRMethod ir;

	
	private Function<Integer, Reference> toReferences = new Function<Integer, Reference>() {
		@Override
		public Reference apply(Integer ref) {
			return new Reference(ref);
		}
	};

	private Function<Use, Iterable<Integer>> extractNewRefs = new Function<Use, Iterable<Integer>>() {
		@Override
		public Iterable<Integer> apply(Use use) {
			return use.getConnectedRefs(label);
		}
	};

	
	public DataFlowGraphBuilder(TypeLabel label, IRMethod ir) {
		this.ir = ir;
		this.label = label;
	}

	public DataFlowGraph createDataFlowGraph() {
		return createDataFlowGraph(label.findTypeUses(ir));
	}
	
	public DataFlowGraph createDataFlowGraph(List<Reference> stringRefs) {
		
		Queue<Reference> refs = new UniqueQueue<Reference>(stringRefs);
		
		Map<Integer, Reference> refMap = Maps.newHashMap();
		
		while(!refs.isEmpty()) {
			Reference stringRef = refs.remove();
			int ref = stringRef.valueNumber();
			
			if (!refMap.containsKey(ref)) {
				refMap.put(ref, stringRef);
			}

			checkDefinition(stringRef);
			checkUses(stringRef);
			
			refs.addAll(findNewRefs(stringRef, refMap.keySet()));
		}

		DataFlowGraph graph = new DataFlowGraph(label, ImmutableMap.copyOf(refMap));
		LOG.debug("created dataflow graph for : {}", graph);
		
		return graph;
	}
	

	private Collection<Reference> findNewRefs(Reference stringRef, Set<Integer> contained) {
 		List<Integer> newRefs = Lists.newLinkedList();
		
		Iterables.addAll(newRefs, stringRef.getDef().getConnectedRefs(label));
		Iterables.addAll(newRefs, concat(transform(stringRef.getUses(), extractNewRefs)));
		
		return Sets.newHashSet(transform(filter(newRefs, not(in(contained))), toReferences));
	}

	private void checkUses(Reference ref) {
		List<SSAInstruction> uses = Lists.newArrayList(ir.getUses(ref.valueNumber()));
		UseFactory useFactory = new UseFactory(ir, ref.valueNumber());
		
		Builder<Use> builder = new ImmutableList.Builder<Use>();
		
		for (SSAInstruction ins : uses) {
			
			builder.add(useFactory.create(ins));
			
		}
		ref.setUses(builder.build());
	}

	private void checkDefinition(Reference ref) {
		
		Definition def = null;
		int v = ref.valueNumber();
		SSAInstruction defInstruction = ir.getDef(v);
		
		if (defInstruction != null) {	
			def = new DefinitionFactory(ir, ref.valueNumber()).create(defInstruction);
			
		} else if (ir.getParams().contains(v)) {
			
			def = Definition.createParamDefinition(ir.getParamIndexFor(v));
		} else if (ir.isConstant(v)) {
			def = Definition.createConstantDefinition(ir.getConstantValue(v));
		}
		
		if (def == null) {
			throw new IllegalStateException("no definition could be found for v=" + v + "(def: " + defInstruction + ")");
		} else {
			ref.setDefinition(def);
		}
		
	}
	
	
}
