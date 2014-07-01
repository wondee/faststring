package de.unifrankfurt.faststring.analysis.util;

import java.util.Collection;
import java.util.List;
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

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;


public class GraphUtil {

	public static final Function<Reference, Integer> referenceToInt = new Function<Reference, Integer>() {
		@Override
		public Integer apply(Reference input) {
			return input.getRef();
		}
	};
	
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
	
	private static final Predicate<Reference> isDefinitionConversation = new Predicate<Reference>() {
		@Override
		public boolean apply(Reference input) {
			return input.isDefinitionConversionToOpt();
		}
	};

	private static Predicate<Reference> isUsesConversation =new Predicate<Reference>() {
		@Override
		public boolean apply(Reference input) {
			return !input.getUseConversionsFromOpt().isEmpty();
		}
	};
	
	public static Iterable<Integer> extractIntsFromStringReferences(Iterable<Reference> refs) {
		return Iterables.transform(refs, referenceToInt);
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
			
			for (int useId : ref.getUseConversionsFromOpt()) {
				useConversations.add(ref.getUses().get(useId));
			}
			
			builder.put(ref.getRef(), useConversations);
		}
		
		return builder.build();
		
	}

	public static Collection<Integer> extractUsageConversionsRefIds(Collection<Reference> refs) {
		return extractUsageConversions(refs).keySet();
	}

	public static Collection<Reference> findReferenceForValueNumbers(
			DataFlowGraph graph, List<Integer> refs) {
		return Collections2.transform(refs, new IntToReference(graph));
	}


}
