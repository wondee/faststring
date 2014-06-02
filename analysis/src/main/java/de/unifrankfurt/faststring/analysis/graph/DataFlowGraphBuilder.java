package de.unifrankfurt.faststring.analysis.graph;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;

import de.unifrankfurt.faststring.analysis.StringCallIdentifier;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;
import de.unifrankfurt.faststring.analysis.util.UniqueQueue;

public class DataFlowGraphBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(DataFlowGraphBuilder.class);
	
	private IR ir;
	private DefUse defUse;
	private StringCallIdentifier identifier;

	private HashSet<Integer> params;

	private Function<Integer, StringReference> toStringReferences = new Function<Integer, StringReference>() {
		@Override
		public StringReference apply(Integer ref) {
			return new StringReference(ref);
		}
	};

	private Function<Use, Iterable<Integer>> extractNewRefs = new Function<Use, Iterable<Integer>>() {
		@Override
		public Iterable<Integer> apply(Use use) {
			return use.getNewRefs();
		}
	};

	
	public DataFlowGraphBuilder(StringCallIdentifier identifier, IR ir, DefUse defUse) {
		this.ir = ir;
		this.defUse = defUse;
		this.identifier = identifier;
		
		params = Sets.newHashSet(Ints.asList(ir.getParameterValueNumbers()));
	}
	
	public DataFlowGraph createDataFlowGraph() {
		
		List<StringReference> stringRefs = findStringUses();
		
		Queue<StringReference> refs = new UniqueQueue<StringReference>(stringRefs);
		
		Map<Integer, StringReference> refMap = Maps.newHashMap();
		
		while(!refs.isEmpty()) {
			StringReference stringRef = refs.remove();
			int ref = stringRef.valueNumber();
			
			if (!refMap.containsKey(ref)) {
				refMap.put(ref, stringRef);
			}

			checkDefinition(stringRef);
			checkUses(stringRef);
			
			refs.addAll(findNewRefs(stringRef, refMap.keySet()));
		}

		DataFlowGraph graph = new DataFlowGraph(identifier.label(), ImmutableMap.copyOf(refMap));
		LOG.debug("created dataflow graph for {}: {}", ir.getMethod().getSignature(), graph);
		
		return graph;
	}
	

	private Collection<StringReference> findNewRefs(StringReference stringRef, Set<Integer> contained) {
 		List<Integer> newRefs = Lists.newLinkedList();
		
		Iterables.addAll(newRefs, stringRef.getDef().getNewRefs());
		Iterables.addAll(newRefs, concat(transform(stringRef.getUses(), extractNewRefs)));
		
		return Sets.newHashSet(transform(filter(newRefs, not(in(contained))), toStringReferences));
	}

	private void checkUses(StringReference ref) {
		List<SSAInstruction> uses = Lists.newArrayList(defUse.getUses(ref.valueNumber()));
		UseFactory useFactory = new UseFactory(ref.valueNumber());
		
		for (SSAInstruction ins : uses) {
			
			ref.addUse(useFactory.create(ins));
		}
	}

	private void checkDefinition(StringReference ref) {
		Definition def = null;
		int v = ref.valueNumber();
		SSAInstruction defInstruction = defUse.getDef(v);
		
		if (defInstruction != null) {	
			def = new DefinitionFactory().create(defInstruction);
			
		} else 	if (params.contains(v)) {
			def = Definition.createParamDefinition();
		} else if (ir.getSymbolTable().isConstant(v)) {
			def = Definition.createConstantDefinition();
		}
		
		if (def == null) {
			throw new IllegalStateException("no definition could be found for v=" + v + "(def: " + defInstruction + ")");
		} else {
			ref.setDefinition(def);
		}
		
	}
	

	private List<StringReference> findStringUses() {
		List<StringReference> stringReference = Lists.newLinkedList();
		
		for (int i = 0; i < ir.getInstructions().length; i++) {
			
			SSAInstruction ins = ir.getInstructions()[i];
			
			int receiver = identifier.check(ins);
			
			if (receiver > -1) {
				stringReference.add(new StringReference(receiver, identifier.label()));
				
			}
							
		}
		return stringReference;
	}
	
	
	
}
