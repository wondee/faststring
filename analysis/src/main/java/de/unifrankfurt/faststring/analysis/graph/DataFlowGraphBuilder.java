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
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SymbolTable;

import de.unifrankfurt.faststring.analysis.label.StringTypeLabel;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;
import de.unifrankfurt.faststring.analysis.util.IRUtil;
import de.unifrankfurt.faststring.analysis.util.UniqueQueue;

public class DataFlowGraphBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(DataFlowGraphBuilder.class);
	
	private StringTypeLabel label;

	private Function<Integer, StringReference> toStringReferences = new Function<Integer, StringReference>() {
		@Override
		public StringReference apply(Integer ref) {
			return new StringReference(ref);
		}
	};

	private Function<Use, Iterable<Integer>> extractNewRefs = new Function<Use, Iterable<Integer>>() {
		@Override
		public Iterable<Integer> apply(Use use) {
			return use.getConnectedRefs();
		}
	};

	
	public DataFlowGraphBuilder(StringTypeLabel label) {
		this.label = label;
		
	}
	
	public DataFlowGraph createDataFlowGraph(DefUse defUse, IR ir) {
		return createDataFlowGraph(defUse, IRUtil.getParamsFromIR(ir), ir.getSymbolTable(), label.findStringUses(ir));
	}
	
	public DataFlowGraph createDataFlowGraph(DefUse defUse, Set<Integer> params, SymbolTable symbolTable, List<StringReference> stringRefs) {
		
		Queue<StringReference> refs = new UniqueQueue<StringReference>(stringRefs);
		
		Map<Integer, StringReference> refMap = Maps.newHashMap();
		
		while(!refs.isEmpty()) {
			StringReference stringRef = refs.remove();
			int ref = stringRef.valueNumber();
			
			if (!refMap.containsKey(ref)) {
				refMap.put(ref, stringRef);
			}

			checkDefinition(defUse, params, symbolTable, stringRef);
			checkUses(defUse, stringRef);
			
			refs.addAll(findNewRefs(stringRef, refMap.keySet()));
		}

		DataFlowGraph graph = new DataFlowGraph(label, ImmutableMap.copyOf(refMap));
		LOG.debug("created dataflow graph for : {}", graph);
		
		return graph;
	}
	

	private Collection<StringReference> findNewRefs(StringReference stringRef, Set<Integer> contained) {
 		List<Integer> newRefs = Lists.newLinkedList();
		
		Iterables.addAll(newRefs, stringRef.getDef().getConnectedRefs());
		Iterables.addAll(newRefs, concat(transform(stringRef.getUses(), extractNewRefs)));
		
		return Sets.newHashSet(transform(filter(newRefs, not(in(contained))), toStringReferences));
	}

	private void checkUses(DefUse defUse, StringReference ref) {
		List<SSAInstruction> uses = Lists.newArrayList(defUse.getUses(ref.valueNumber()));
		UseFactory useFactory = new UseFactory(ref.valueNumber());
		
		Builder<Use> builder = new ImmutableList.Builder<Use>();
		
		for (SSAInstruction ins : uses) {
			
			builder.add(useFactory.create(ins));
			
		}
		ref.setUses(builder.build());
	}

	private void checkDefinition(DefUse defUse, Set<Integer> params, SymbolTable symbolTable, StringReference ref) {
		Definition def = null;
		int v = ref.valueNumber();
		SSAInstruction defInstruction = defUse.getDef(v);
		
		if (defInstruction != null) {	
			def = new DefinitionFactory().create(defInstruction);
			
		} else 	if (params.contains(v)) {
			def = Definition.createParamDefinition();
		} else if (symbolTable.isConstant(v)) {
			def = Definition.createConstantDefinition();
		}
		
		if (def == null) {
			throw new IllegalStateException("no definition could be found for v=" + v + "(def: " + defInstruction + ")");
		} else {
			ref.setDefinition(def);
		}
		
	}
	
	
	
}
