package de.unifrankfurt.faststring.analysis.util;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import de.unifrankfurt.faststring.analysis.graph.Reference;


public class GraphUtil {

	public static final Function<Reference, Integer> referenceToInt = new Function<Reference, Integer>() {
		@Override
		public Integer apply(Reference input) {
			return input.valueNumber();
		}
	};


	public static Collection<Integer> extractIntsFromStringReferences(Collection<Reference> refs) {
		return Collections2.transform(refs, referenceToInt);
	}




}
