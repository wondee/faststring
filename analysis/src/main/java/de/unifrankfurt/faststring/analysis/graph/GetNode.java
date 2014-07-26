package de.unifrankfurt.faststring.analysis.graph;

public class GetNode extends InstructionNode {

	@Override
	public void visit(Visitor visitor) {
		visitor.visitGet(this);
	}

}
