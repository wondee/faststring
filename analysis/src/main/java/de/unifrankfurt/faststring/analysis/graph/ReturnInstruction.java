package de.unifrankfurt.faststring.analysis.graph;

public class ReturnInstruction extends InstructionNode {

	private int result;

	public ReturnInstruction(int result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ReturnInstruction [result=" + result + ", getByteCodeIndex()="
				+ getByteCodeIndex() + ", getLocalVariableIndex()="
				+ getLocalVariableIndex(result) + "]";
	}

	@Override
	public void visit(Visitor visitor) {
		visitor.visitReturn(this);
	}

}
