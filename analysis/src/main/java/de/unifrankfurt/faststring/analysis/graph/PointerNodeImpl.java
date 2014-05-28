package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;


public class PointerNodeImpl implements PointerNode {
	
	
	private int valueNumber;

	private Set<Integer> succNodes = Sets.newHashSet();
	private Set<Integer> predNodes = Sets.newHashSet();
	
	public PointerNodeImpl(int valueNumber, Set<Integer> succNodes, Set<Integer> predNodes) {
		this.valueNumber = valueNumber;
		this.succNodes = succNodes;
		this.predNodes = predNodes;
	}

	@Override
	public Integer valueNumber() {
		return valueNumber;
	}

	public Set<Integer> getSuccessors() {
		return predNodes;
	}

	/* (non-Javadoc)
	 * @see de.unifrankfurt.faststring.analysis.graph.PointerNode#hasSuccessor(de.unifrankfurt.faststring.analysis.graph.PointerNode)
	 */
	@Override
	public boolean hasSuccessor(Integer node) {
		return succNodes.contains(node);
	}

	/* (non-Javadoc)
	 * @see de.unifrankfurt.faststring.analysis.graph.PointerNode#hasPredecessor(de.unifrankfurt.faststring.analysis.graph.PointerNode)
	 */
	@Override
	public boolean hasPredecessor(Integer node) {
		return predNodes.contains(node);
	}
	
	@Override
	public Collection<Integer> getPredeseccors() {
		return predNodes;
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
		PointerNodeImpl other = (PointerNodeImpl) obj;
		if (valueNumber != other.valueNumber)
			return false;
		return true;
	}

	@Override
	public String toString() {
		
		return "PointerNode [valueNumber=" + valueNumber + ", succNodes="
				+ succNodes + ", predNodes=" + predNodes + "]";
	}



	

	
	
}
