package de.unifrankfurt.faststring.analysis.graph;


public interface CreationStrategy<RETURN, PARAM> {

	RETURN create(PARAM ins);

}