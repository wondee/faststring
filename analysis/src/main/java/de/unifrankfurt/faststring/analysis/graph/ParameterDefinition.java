package de.unifrankfurt.faststring.analysis.graph;


public class ParameterDefinition extends InstructionNode {

	private int index;

	public ParameterDefinition(int paramIndexFor) {
		this.index = paramIndexFor;
	}

	@Override
	public String toString() {
		return "ParameterDefinition [index=" + index + "]";
	}

	
}
