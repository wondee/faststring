package de.unifrankfurt.faststring.analysis.model;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public class MethodParameterDefinition extends Definition {

	@Override
	public String toString() {
		return "MethodParameterDefinition";
	}

	@Override
	public boolean isCompatibleWith(TypeLabel label) {
		return false;
	}

}
