package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;


public class ParameterDefinition extends InstructionNode {

	private int index;

	public ParameterDefinition(int paramIndexFor) {
		this.index = paramIndexFor;
	}

	@Override
	public Collection<Integer> getLocalVariableIndex(Integer v) {
		Collection<Integer> localVariableIndex = super.getLocalVariableIndex(v);
		localVariableIndex.add(index);
		return localVariableIndex;
	}
	
	@Override
	public String toString() {
		return "ParameterDefinition [index=" + index + "]";
	}

	
}
