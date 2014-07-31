package de.unifrankfurt.faststring.analysis.graph;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public abstract class NotLabelableNode extends InstructionNode {

	@Override
	protected boolean isIndexCompatibleWithActual(TypeLabel label, int inV) {
		return label == null;
	}
	
	@Override
	protected boolean isDefCompatibleWithActual(TypeLabel label) {
		return label == null;
	}
}
