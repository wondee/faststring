package de.unifrankfurt.faststring.analysis.util;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

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


	public static Collection<Integer> extractIntsFromStringReferences(Collection<Reference> refs) {
		return Collections2.transform(refs, referenceToInt);
	}


	public static Map<Integer, InstructionNode> extractDefConversionsFromOpt(Collection<Reference> candidates, TypeLabel label) {
		return filterAndCreateMap(candidates, new IsDefinitionConversationFromOpt(label), referenceToDefinition);
	}

	public static Map<Integer, InstructionNode> extractDefConversionsToOpt(Collection<Reference> candidates, TypeLabel label) {
		return filterAndCreateMap(candidates, new IsDefinitionConversationToOpt(label), referenceToDefinition);
	}

	private static <T> Map<Integer, T> filterAndCreateMap(Iterable<Reference> refs, Predicate<Reference> filter, Function<Reference, T> function) {
		Map<Integer, T> defMap = Maps.newHashMap();

		for (Reference ref : Iterables.filter(refs, filter)) {
			defMap.put(ref.valueNumber(), function.apply(ref));
		}

		return defMap;
	}



}
