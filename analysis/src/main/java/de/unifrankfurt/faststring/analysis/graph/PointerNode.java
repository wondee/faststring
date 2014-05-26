package de.unifrankfurt.faststring.analysis.graph;

import java.util.Set;

import com.google.common.collect.Sets;


public class PointerNode {
	
	
	private int valueNumber;

	private Set<PointerNode> succNodes = Sets.newHashSet();
	private Set<PointerNode> predNodes = Sets.newHashSet();
	
	public PointerNode(int valueNumber) {
		this.valueNumber = valueNumber;
	}

	public Integer valueNumber() {
		return valueNumber;
	}

	public void addSuccessor(PointerNode node) {
		succNodes.add(node);
		
	}

	public void addPredecessor(PointerNode node) {
		predNodes.add(node);
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + valueNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PointerNode other = (PointerNode) obj;
		if (valueNumber != other.valueNumber)
			return false;
		return true;
	}
	

	
	
}
