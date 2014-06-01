package de.unifrankfurt.faststring.analysis.graph;

import java.util.Map;

import com.google.common.collect.Maps;

public class DataFlowGraph {
	
	Map<Integer, StringReference> nodes = Maps.newHashMap();
	
	public DataFlowGraph(Map<Integer, StringReference> nodeMap) {
		nodes = nodeMap;
	}

	public boolean contains(Integer key) {
		return nodes.containsKey(key);
	}

	public StringReference get(int ref) {
		return nodes.get(ref);
	}

//	public boolean hasEdge(int srcNode, int targetNode) {
//		return GraphUtil.hasEdge(nodes.get(srcNode), nodes.get(targetNode));
//	}

	public int size() {
		return nodes.size();
	}

//	public List<Integer> findAllSuccessors(int valueNumber) {
//		return findAll(EdgeType.S, valueNumber);
//	}
//	
//	public List<Integer> findAllPredeseccors(int valueNumber) {
//		return findAll(EdgeType.P, valueNumber);
//	}
//
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
//	
//	private List<Integer> findAllWithSelf(EdgeType type, int valueNumber) {
//		List<Integer> list = findAll(type, valueNumber);
//		list.add(valueNumber);
//		
//		return list;
//	}
//	
//	public List<Integer> findAllPredeseccorsWithSelf(int valueNumber) {
//		return findAllWithSelf(EdgeType.P, valueNumber);
//	}
//	
//	public List<Integer> findAllSuccessorsWithSelf(int valueNumber) {
//		return findAllWithSelf(EdgeType.S, valueNumber);
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