package de.unifrankfurt.faststring.analysis.graph;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ibm.wala.ssa.SSAInstruction;

import de.unifrankfurt.faststring.analysis.IRMethod;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.util.UniqueQueue;

public class DataFlowGraphBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(DataFlowGraphBuilder.class);
	
	private final TypeLabel label;
	private final IRMethod ir;
	
	private InstructionNodeFactory instructionFactory;
	
	private Function<Integer, Reference> toReferences = new Function<Integer, Reference>() {
		@Override
		public Reference apply(Integer ref) {
			return new Reference(ref);
		}
	};


//	private Function<InstructionNode, Iterable<Integer>> extractNewRefs = new Function<InstructionNode, Iterable<Integer>>() {
//		@Override
//		public Iterable<Integer> apply(InstructionNode use) {
//			return use.getConnectedRefs(label);
//		}
//	};

	
	public DataFlowGraphBuilder(TypeLabel label, IRMethod ir) {
		this.ir = ir;
		this.label = label;
		this.instructionFactory = new InstructionNodeFactory(ir);
	}

	public DataFlowGraph createDataFlowGraph() {
		return createDataFlowGraph(label.findTypeUses(ir));
	}
	
	public DataFlowGraph createDataFlowGraph(Collection<Reference> stringRefs) {
		
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
	

	private Collection<Reference> findNewRefs(Reference ref, Set<Integer> contained) {
		
		return Sets.newHashSet(transform(filter(ref.getConnectedRefs(label), not(in(contained))), toReferences));
		
	}

	private void checkUses(Reference ref) {
		List<SSAInstruction> uses = Lists.newArrayList(ir.getUses(ref.valueNumber()));

		Builder<InstructionNode> builder = new ImmutableList.Builder<InstructionNode>();
		
		for (SSAInstruction ins : uses) {
			
			builder.add(instructionFactory.create(ins));
			
		}
		ref.setUses(builder.build());
	}

	private void checkDefinition(Reference ref) {
		
		int v = ref.valueNumber();
		SSAInstruction instruction = ir.getDef(v);
		
		InstructionNode definition = null;
		
		if (instruction == null) {
			if (ir.getParams().contains(v)) {
				definition = new ParameterDefinition(ir.getParamIndexFor(v));
			} else if (ir.isConstant(v)) {
				definition = instructionFactory.createConstant(v);
			}
		} else {
			definition = instructionFactory.create(instruction);	
		}
		
		if (definition == null) {
			throw new IllegalStateException("no definition could be found for v=" + v + "(def: " + instruction + ")");
		} else {
			ref.setDefinition(definition);
		}
		
		
	}
	
	
}
