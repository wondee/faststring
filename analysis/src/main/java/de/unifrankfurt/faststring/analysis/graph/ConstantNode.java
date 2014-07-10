package de.unifrankfurt.faststring.analysis.graph;



public class ConstantNode extends InstructionNode {

	private Object value;

	public ConstantNode(Object value, int bcIndex) {
		this.value = value;
		setByteCodeIndex(bcIndex);
	}

	public Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "ConstantDefinition [value=" + value + ", bcIndex=" + getByteCodeIndex() + ", locals=" + localMap + "]";
	}

	@Override
	public void visit(Visitor visitor) {
		visitor.visitConstant(this);
	}




}
