package de.unifrankfurt.faststring.analysis.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;


public class GraphUtil {

	public static final Function<Reference, Integer> stringReferenceToInt = new Function<Reference, Integer>() {
		@Override
		public Integer apply(Reference input) {
			return input.getRef();
		}
	};
	
	private static final Predicate<Reference> isDefinitionConversation = new Predicate<Reference>() {
		@Override
		public boolean apply(Reference input) {
			return input.isDefinitionConversion();
		}
	};

	private static Predicate<Reference> isUsesConversation =new Predicate<Reference>() {
		@Override
		public boolean apply(Reference input) {
			return !input.getUseConversions().isEmpty();
		}
	};
	
	public static Iterable<Integer> extractIntsFromStringReferences(Iterable<Reference> refs) {
		return Iterables.transform(refs, stringReferenceToInt);
	}

	public static Map<Integer, Definition> extractDefConversions(Collection<Reference> candidates) {
		
		Iterable<Reference> refs = Collections2.filter(candidates, isDefinitionConversation);
		
		Map<Integer, Definition> defMap = Maps.newHashMap();
		
		for (Reference ref : refs) {
			defMap.put(ref.valueNumber(), ref.getDef());
		}
		
		return defMap;
	}
	
	public static Iterable<Integer> extractDefConversionsAsInt(Collection<Reference> candidates) {
		return extractIntsFromStringReferences(Collections2.filter(candidates, isDefinitionConversation));
	}

	public static Map<Integer, Set<Use>> extractUsageConversions(Collection<Reference> finalRefs) {
		
		Collection<Reference> refsWithUseConversation = Collections2.filter(finalRefs, isUsesConversation);
		Builder<Integer, Set<Use>> builder = new ImmutableMap.Builder<Integer, Set<Use>>();
		
		for (Reference ref : refsWithUseConversation) {
			Set<Use> useConversations = Sets.newHashSet();
			
			for (int useId : ref.getUseConversions()) {
				useConversations.add(ref.getUses().get(useId));
			}
			
			builder.put(ref.getRef(), useConversations);
		}
		
		return builder.build();
		
	}

	public static Collection<Integer> extractUsageConversionsRefIds(Collection<Reference> refs) {
		return extractUsageConversions(refs).keySet();
	}


}
