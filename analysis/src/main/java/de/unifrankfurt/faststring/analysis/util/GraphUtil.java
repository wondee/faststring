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


	public static Collection<Integer> extractIntsFromStringReferences(Collection<Reference> refs) {
		return Collections2.transform(refs, referenceToInt);
	}


	@SuppressWarnings("unused")
	private static <T> Map<Integer, T> filterAndCreateMap(Iterable<Reference> refs, Predicate<Reference> filter, Function<Reference, T> function) {
		Map<Integer, T> defMap = Maps.newHashMap();

		for (Reference ref : Iterables.filter(refs, filter)) {
			defMap.put(ref.valueNumber(), function.apply(ref));
		}

		return defMap;
	}



}
