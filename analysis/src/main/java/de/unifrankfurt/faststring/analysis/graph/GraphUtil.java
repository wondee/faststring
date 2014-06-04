package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import de.unifrankfurt.faststring.analysis.model.Use;


public class GraphUtil {

	public static final Function<StringReference, Integer> stringReferenceToInt = new Function<StringReference, Integer>() {
		@Override
		public Integer apply(StringReference input) {
			return input.getRef();
		}
	};
	
	private static final Predicate<StringReference> isDefinitionConversation = new Predicate<StringReference>() {
		@Override
		public boolean apply(StringReference input) {
			return input.isDefinitionConversion();
		}
	};

	private static Predicate<StringReference> isUsesConversation =new Predicate<StringReference>() {
		@Override
		public boolean apply(StringReference input) {
			return !input.getUseConversions().isEmpty();
		}
	};
	
	public static Iterable<Integer> extractIntsFromStringReferences(Iterable<StringReference> refs) {
		return Iterables.transform(refs, stringReferenceToInt);
	}

	public static Iterable<Integer> extractDefConversions(Collection<StringReference> candidates) {
		return extractIntsFromStringReferences(Collections2.filter(candidates, isDefinitionConversation));
	}

	public static Map<Integer, Set<Use>> extractUsageConversions(Collection<StringReference> finalRefs) {
		
		Collection<StringReference> refsWithUseConversation = Collections2.filter(finalRefs, isUsesConversation);
		Builder<Integer, Set<Use>> builder = new ImmutableMap.Builder<Integer, Set<Use>>();
		
		for (StringReference ref : refsWithUseConversation) {
			Set<Use> useConversations = Sets.newHashSet();
			
			for (int useId : ref.getUseConversions()) {
				useConversations.add(ref.getUses().get(useId));
			}
			
			builder.put(ref.getRef(), useConversations);
		}
		
		return builder.build();
		
	}


}
