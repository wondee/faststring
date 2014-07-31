package de.unifrankfurt.faststring.analysis.graph;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class ConditionalBranchNode extends InstructionNode {

	@Override
	public void visit(Visitor visitor) {
		visitor.visitBranch(this);

	}
	
	@Override
	protected boolean isCompatibleWithActual(TypeLabel label, int inV) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
