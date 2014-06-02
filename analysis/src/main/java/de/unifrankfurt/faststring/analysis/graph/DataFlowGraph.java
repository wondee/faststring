package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import de.unifrankfurt.faststring.analysis.label.Label;

public class DataFlowGraph {
	
	private final Map<Integer, StringReference> nodes;
	private final Label label;
	private final Predicate<StringReference> matchedLabel = new Predicate<StringReference>() {
		@Override
		public boolean apply(StringReference ref) {
			return label == ref.getLabel();
		}
	};
	
	public DataFlowGraph(Label label, Map<Integer, StringReference> nodeMap) {
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



//	private List<Integer> findAll(EdgeType type, int valueNumber) {
//		Set<Integer> nodes = Sets.newHashSet();
//		Set<Integer> checkedNumbers = Sets.newHashSet();
//		Stack<Integer> numbersToCheck = new Stack<Integer>();
//		
//		numbersToCheck.push(valueNumber);
//
//		while (!numbersToCheck.isEmpty()) {
//			int currentNumber = numbersToCheck.pop();
//			
//			if (!checkedNumbers.contains(currentNumber)) {
//				PointerNodeImpl node = (PointerNodeImpl)get(currentNumber);
//				
//				for (Integer i : node.getEdges(type)) {
//					numbersToCheck.push(i);
//					nodes.add(i);
//				}
//				
//				checkedNumbers.add(currentNumber);
//			}
//			
//		}
//		
//		return Lists.newLinkedList(nodes);
//	}

	
	
	@Override
	public String toString() {
		String linebreak = System.getProperty("line.separator");
		StringBuilder builder = new StringBuilder(linebreak);
		
		for (StringReference node : nodes.values()) {
			builder
				.append("  ")
				.append(node)
				.append(linebreak);
		}
		
		return builder.toString();
	}



}