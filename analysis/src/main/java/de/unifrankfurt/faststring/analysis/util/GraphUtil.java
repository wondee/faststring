package de.unifrankfurt.faststring.analysis.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;


public class GraphUtil {

	public static final Function<Reference, Integer> referenceToInt = new Function<Reference, Integer>() {
		@Override
		public Integer apply(Reference input) {
			return input.getRef();
		}
	};


	public static final Function<Reference, InstructionNode> referenceToDefinition = new Function<Reference, InstructionNode>() {
		@Override
		public InstructionNode apply(Reference input) {
			return input.getDefinition();
		}
	};

	public static final Function<Reference, Set<Integer>> referenceToUsageConversationToOpt = new Function<Reference, Set<Integer>>() {
		@Override
		public Set<Integer> apply(Reference input) {
			return input.getUseConversionsToOpt();
		}
	};

	public static final Function<Reference, Set<Integer>> referenceToUsageConversationFromOpt = new Function<Reference, Set<Integer>>() {
		@Override
		public Set<Integer> apply(Reference input) {
			return input.getUseConversionsFromOpt();
		}
	};

	private static final Predicate<Reference> isDefinitionConversationToOpt = new Predicate<Reference>() {
		@Override
		public boolean apply(Reference input) {
			return input.isDefinitionConversionToOpt();
		}
	};


	private static final Predicate<Reference> isDefinitionConversationFromOpt = new Predicate<Reference>() {
		@Override
		public boolean apply(Reference input) {
			return input.isDefinitionConversionFromOpt();
		}
	};


	private static Predicate<Reference> isUseConversationToOpt = new Predicate<Reference>() {
		@Override
		public boolean apply(Reference input) {
			return !input.getUseConversionsToOpt().isEmpty();
		}
	};


	private static Predicate<Reference> isUseConversationFromOpt = new Predicate<Reference>() {
		@Override
		public boolean apply(Reference input) {
			return !input.getUseConversionsFromOpt().isEmpty();
		}
	};

	public static Collection<Integer> extractIntsFromStringReferences(Collection<Reference> refs) {
		return Collections2.transform(refs, referenceToInt);
	}

	public static Collection<Reference> extractReferencesWithDefConversionsToOpt(Collection<Reference> candidates) {
		return Collections2.filter(candidates, isDefinitionConversationToOpt);
	}

	public static Collection<Reference> extractReferencesWithDefConversionsFromOpt(Collection<Reference> candidates) {
		return Collections2.filter(candidates, isDefinitionConversationFromOpt);
	}

	public static Iterable<Integer> extractReferencesWithDefConversionsToOptAsInt(Collection<Reference> candidates) {
		return extractIntsFromStringReferences(extractReferencesWithDefConversionsToOpt(candidates));
	}

	public static Iterable<Integer> extractReferencesWithDefConversionsFromOptAsInt(Collection<Reference> candidates) {
		return extractIntsFromStringReferences(extractReferencesWithDefConversionsFromOpt(candidates));
	}

	public static Map<Integer, InstructionNode> extractDefConversionsFromOpt(Collection<Reference> candidates) {
		return filterAndCreateMap(candidates, isDefinitionConversationFromOpt, referenceToDefinition);
	}

	public static Map<Integer, InstructionNode> extractDefConversionsToOpt(Collection<Reference> candidates) {
		return filterAndCreateMap(candidates, isDefinitionConversationToOpt, referenceToDefinition);
	}

	public static Map<Integer, Set<Integer>> extractUseConversationsToOpt(Collection<Reference> candidates) {
		return filterAndCreateMap(candidates, isUseConversationToOpt, referenceToUsageConversationToOpt);
	}

	private static <T> Map<Integer, T> filterAndCreateMap(Iterable<Reference> refs, Predicate<Reference> filter, Function<Reference, T> function) {
		Map<Integer, T> defMap = Maps.newHashMap();

		for (Reference ref : refs) {
			defMap.put(ref.valueNumber(), function.apply(ref));
		}

		return defMap;
	}

	public static Collection<Reference> findReferenceForValueNumbers(
			DataFlowGraph graph, List<Integer> refs) {
		return Collections2.transform(refs, new IntToReference(graph));
	}

	private static class IntToReference implements Function<Integer, Reference> {

		private DataFlowGraph graph;

		public IntToReference(DataFlowGraph graph) {
			this.graph = graph;
		}
		@Override
		public Reference apply(Integer i) {
			return graph.get(i);
		}

	}


}
