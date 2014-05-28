package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;

public interface PointerNode {

	Integer valueNumber();

	boolean hasSuccessor(Integer node);
	boolean hasPredecessor(Integer node);

	Collection<Integer> getPredeseccors();

}