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
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ibm.wala.ssa.SSAInstruction;

import de.unifrankfurt.faststring.analysis.AnalyzedMethod;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.util.UniqueQueue;

public class DataFlowGraphBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(DataFlowGraphBuilder.class);
	
	private final AnalyzedMethod method;

	private InstructionNodeFactory instructionFactory;

	private Function<Integer, Reference> toReferences = new Function<Integer, Reference>() {
		@Override
		public Reference apply(Integer ref) {
			return new Reference(ref);
		}
	};

	public DataFlowGraphBuilder(AnalyzedMethod method) {
		this.method = method;
		this.instructionFactory = new InstructionNodeFactory(method);
	}

	public DataFlowGraph createDataFlowGraph(TypeLabel label) {
		return createDataFlowGraph(label.findTypeUses(method), Sets.newHashSet(label));
	}

	public DataFlowGraph createDataFlowGraph(Collection<Reference> stringRefs, Collection<TypeLabel> labels) {

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

			refs.addAll(findNewRefs(stringRef, refMap.keySet(), labels));
		}

		DataFlowGraph graph = new DataFlowGraph(ImmutableMap.copyOf(refMap));
		LOG.debug("created dataflow graph for : {}", graph);

		return graph;
	}


	private Collection<Reference> findNewRefs(Reference ref, Set<Integer> contained, Collection<TypeLabel> labels) {
		Set<Reference> newRefs = Sets.newHashSet();
		
		for (TypeLabel label : labels) {
			Set<Reference> refs = Sets.newHashSet(transform(filter(ref.getConnectedRefs(label), not(in(contained))), toReferences));
			newRefs.addAll(refs);
		}
		return newRefs;

	}

	private void checkUses(Reference ref) {
		List<SSAInstruction> uses = method.getUses(ref.valueNumber());

		Builder<InstructionNode> builder = new ImmutableList.Builder<InstructionNode>();

		for (SSAInstruction ins : uses) {
			InstructionNode use = instructionFactory.create(ins);
			builder.add(use);

		}
		ref.setUses(builder.build());
	}

	private void checkDefinition(Reference ref) {

		int v = ref.valueNumber();
		SSAInstruction instruction = method.getDef(v);

		InstructionNode definition = null;

		if (instruction == null) {
			if (method.getParams().contains(v)) {
				definition = new ParameterNode(method.getParamIndexFor(v));
			} else if (method.isConstant(v)) {
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
