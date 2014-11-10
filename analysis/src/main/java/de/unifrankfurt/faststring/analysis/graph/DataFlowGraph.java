package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import de.unifrankfurt.faststring.analysis.util.StringUtil;

/**
 * represents a dataflowgraph of a method.
 * <p>
 *
 *
 * @author markus
 *
 */
public class DataFlowGraph {

	private final Map<Integer, Reference> nodes;
	private static final Predicate<Reference> matchedLabel = new Predicate<Reference>() {
		@Override
		public boolean apply(Reference ref) {
			return ref.getLabel() != null;
		}
	};

	public DataFlowGraph(Map<Integer, Reference> nodeMap) {
		this.nodes = nodeMap;
	}

	public boolean contains(Integer key) {
		return nodes.containsKey(key);
	}

	public Reference get(int ref) {
		return nodes.get(ref);
	}

	public int size() {
		return nodes.size();
	}


	public Collection<Reference> getAllLabelMatchingReferences() {
		return Maps.filterValues(nodes, matchedLabel).values();
	}


	@Override
	public String toString() {
		return StringUtil.toStringWithLineBreak(nodes.values());
	}

	public Collection<Reference> getReferences() {
		return nodes.values();
	}

	public boolean isEmpty() {
		return nodes.isEmpty();
	}



}