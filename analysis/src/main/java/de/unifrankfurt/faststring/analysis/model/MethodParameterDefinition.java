package de.unifrankfurt.faststring.analysis.model;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public class MethodParameterDefinition extends Definition {

	private int index;

	public MethodParameterDefinition(int i) {
		this.index = i;
	}

	@Override
	public String toString() {
		return "MethodParameterDefinition [index=" + index + "]";
	}

	@Override
	public boolean isCompatibleWith(TypeLabel label) {
		return false;
	}

}
