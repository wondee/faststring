package de.unifrankfurt.faststring.analysis.graph;



public class ConstantNode extends InstructionNode {

	private Object value;

	public ConstantNode(int bcIndex) {
		setByteCodeIndex(bcIndex);
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
