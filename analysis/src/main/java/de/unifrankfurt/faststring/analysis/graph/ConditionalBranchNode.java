package de.unifrankfurt.faststring.analysis.graph;

public class ConditionalBranchNode extends InstructionNode {

	@Override
	public void visit(Visitor visitor) {
		visitor.visitBranch(this);

	}

}
