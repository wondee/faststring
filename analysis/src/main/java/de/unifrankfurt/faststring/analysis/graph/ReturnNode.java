package de.unifrankfurt.faststring.analysis.graph;

public class ReturnNode extends InstructionNode {

	private int result;

	public ReturnNode(int result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ReturnInstruction [result=" + result + ", getByteCodeIndex()="
				+ getByteCodeIndex() + ", getLocalVariableIndex()="
				+ getLocals(result) + "]";
	}

	@Override
	public void visit(Visitor visitor) {
		visitor.visitReturn(this);
	}

}
