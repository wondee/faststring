package de.unifrankfurt.faststring.analysis.graph;


public class ConstantDefinition extends InstructionNode {

	private Object value;
	
	public ConstantDefinition(Object value) {
		this.value = value;
	}
	

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "ConstantDefinition [value=" + value + "]";
	}

}
