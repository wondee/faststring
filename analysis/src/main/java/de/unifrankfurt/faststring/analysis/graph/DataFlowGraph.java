package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import de.unifrankfurt.faststring.analysis.label.StringTypeLabel;
import de.unifrankfurt.faststring.analysis.util.StringUtil;

public class DataFlowGraph {
	
	private final Map<Integer, StringReference> nodes;
	private final StringTypeLabel label;
	private final Predicate<StringReference> matchedLabel = new Predicate<StringReference>() {
		@Override
		public boolean apply(StringReference ref) {
			return label == ref.getLabel();
		}
	};
	
	public DataFlowGraph(StringTypeLabel label, Map<Integer, StringReference> nodeMap) {
		this.nodes = nodeMap;
		this.label = label;
	}

	public boolean contains(Integer key) {
		return nodes.containsKey(key);
	}

	public StringReference get(int ref) {
		return nodes.get(ref);
	}

	public int size() {
		return nodes.size();
	}
	

	public Collection<StringReference> getAllLabelMatchingReferences() {
		return Maps.filterValues(nodes, matchedLabel).values();
	}

	
	@Override
	public String toString() {
		return StringUtil.toStringWithLineBreak(nodes.values());
	}



}