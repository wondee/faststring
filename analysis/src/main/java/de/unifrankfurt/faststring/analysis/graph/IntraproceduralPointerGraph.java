package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class IntraproceduralPointerGraph {
	
	Map<Integer, PointerNode> nodes = Maps.newHashMap();
	
	public IntraproceduralPointerGraph(Map<Integer, PointerNode> nodeMap) {
		nodes = nodeMap;
	}

	public boolean contains(Integer key) {
		return nodes.containsKey(key);
	}

	public PointerNode get(int ref) {
		return nodes.get(ref);
	}

	public boolean hasEdge(int srcNode, int targetNode) {
		return GraphUtil.hasEdge(nodes.get(srcNode), nodes.get(targetNode));
	}

	public int size() {
		return nodes.size();
	}

	public List<Integer> findAllPredeseccors(int valueNumber) {
		Set<Integer> preNodes = Sets.newHashSet();
		Set<Integer> checkedNumbers = Sets.newHashSet();
		Stack<Integer> numbersToCheck = new Stack<Integer>();
		
		numbersToCheck.push(valueNumber);

		while (!numbersToCheck.isEmpty()) {
			int currentNumber = numbersToCheck.pop();
			
			if (!checkedNumbers.contains(currentNumber)) {
				PointerNode node = get(currentNumber);
				
				for (Integer i : node.getPredeseccors()) {
					numbersToCheck.push(i);
					preNodes.add(i);
				}
				
				checkedNumbers.add(currentNumber);
			}
			
		}
		
		return Lists.newLinkedList(preNodes);
	}
	
	@Override
	public String toString() {
		String linebreak = System.getProperty("line.separator");
		StringBuilder builder = new StringBuilder(linebreak);
		
		for (PointerNode node : nodes.values()) {
			builder
				.append("  ")
				.append(node)
				.append(linebreak);
		}
		
		return builder.toString();
	}
	
}