package de.unifrankfurt.faststring.analysis.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public class GraphUtil {

	public static final Function<Reference, Integer> referenceToInt = new Function<Reference, Integer>() {
		@Override
		public Integer apply(Reference input) {
			return input.valueNumber();
		}
	};


	public static final Function<Reference, InstructionNode> referenceToDefinition = new Function<Reference, InstructionNode>() {
		@Override
		public InstructionNode apply(Reference input) {
			return input.getDefinition();
		}
	};

	private static abstract class BaseLabelFunction {
		
		TypeLabel label;

		public BaseLabelFunction(TypeLabel label) {
			this.label = label;
		}

	}
	
	private static final class ReferenceToUsageConversationToOpt extends BaseLabelFunction 
		implements Function<Reference, Collection<Integer>> {
		public ReferenceToUsageConversationToOpt(TypeLabel label) {
			super(label);
		}

		@Override
		public Collection<Integer> apply(Reference input) {
			return input.getUseConversionsToOpt(label);
		}
		
	}

//	private static final class ReferenceToUsageConversationFromOpt extends BaseLabelFunction 
//		implements Function<Reference, Collection<Integer>> {
//		public ReferenceToUsageConversationFromOpt(TypeLabel label) {
//			super(label);
//		}
//
//		@Override
//		public Collection<Integer> apply(Reference input) {
//			return input.getUseConversionsFromOpt(label);
//		}
//		
//	}

	private static final class IsDefinitionConversationToOpt extends BaseLabelFunction implements Predicate<Reference>  {
		public IsDefinitionConversationToOpt(TypeLabel label) {
			super(label);
		}

		@Override
		public boolean apply(Reference input) {
			return input.isDefinitionConversionToOpt(label);
		}
	}

	private static final class IsDefinitionConversationFromOpt extends BaseLabelFunction implements Predicate<Reference>  {
		public IsDefinitionConversationFromOpt(TypeLabel label) {
			super(label);
		}

		@Override
		public boolean apply(Reference input) {
			return input.isDefinitionConversionFromOpt(label);
		}
	}

	private static final class IsUseConversationToOpt extends BaseLabelFunction implements Predicate<Reference>  {
		public IsUseConversationToOpt(TypeLabel label) {
			super(label);
		}

		@Override
		public boolean apply(Reference input) {
			return !input.getUseConversionsToOpt(label).isEmpty();
		}
	}

//	private static final class IsUseConversationFromOpt extends BaseLabelFunction implements Predicate<Reference>  {
//		public IsUseConversationFromOpt(TypeLabel label) {
//			super(label);
//		}
//
//		@Override
//		public boolean apply(Reference input) {
//			return !input.getUseConversionsFromOpt(label).isEmpty();
//		}
//	}


	public static Collection<Integer> extractIntsFromStringReferences(Collection<Reference> refs) {
		return Collections2.transform(refs, referenceToInt);
	}

	public static Collection<Reference> extractReferencesWithDefConversionsToOpt(Collection<Reference> candidates, TypeLabel label) {
		return Collections2.filter(candidates, new IsDefinitionConversationToOpt(label));
	}

	public static Collection<Reference> extractReferencesWithDefConversionsFromOpt(Collection<Reference> candidates, TypeLabel label) {
		return Collections2.filter(candidates, new IsDefinitionConversationFromOpt(label));
	}

	public static Iterable<Integer> extractReferencesWithDefConversionsToOptAsInt(Collection<Reference> candidates, TypeLabel label) {
		return extractIntsFromStringReferences(extractReferencesWithDefConversionsToOpt(candidates, label));
	}

	public static Iterable<Integer> extractReferencesWithDefConversionsFromOptAsInt(Collection<Reference> candidates, TypeLabel label) {
		return extractIntsFromStringReferences(extractReferencesWithDefConversionsFromOpt(candidates, label));
	}

	public static Map<Integer, InstructionNode> extractDefConversionsFromOpt(Collection<Reference> candidates, TypeLabel label) {
		return filterAndCreateMap(candidates, new IsDefinitionConversationFromOpt(label), referenceToDefinition);
	}

	public static Map<Integer, InstructionNode> extractDefConversionsToOpt(Collection<Reference> candidates, TypeLabel label) {
		return filterAndCreateMap(candidates, new IsDefinitionConversationToOpt(label), referenceToDefinition);
	}

	public static Map<Integer, Collection<Integer>> extractUseConversationsToOpt(Collection<Reference> candidates, TypeLabel label) {
		return filterAndCreateMap(candidates, new IsUseConversationToOpt(label), new ReferenceToUsageConversationToOpt(label));
	}

	private static <T> Map<Integer, T> filterAndCreateMap(Iterable<Reference> refs, Predicate<Reference> filter, Function<Reference, T> function) {
		Map<Integer, T> defMap = Maps.newHashMap();

		for (Reference ref : Iterables.filter(refs, filter)) {
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
