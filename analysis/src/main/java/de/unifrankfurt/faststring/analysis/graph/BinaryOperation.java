package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;

public class BinaryOperation extends InstructionNode {

	public BinaryOperation(int def, List<Integer> uses) {
		setDef(def);
		setUses(uses);
	}

	@Override
	public void visit(Visitor visitor) {
		visitor.visitBinaryOperator(this);
	}

}
