package de.unifrankfurt.faststring.analysis.model;

import de.unifrankfurt.faststring.analysis.label.Label;


public class MethodParameterDefinition extends Definition {

	@Override
	public String toString() {
		return "MethodParameterDefinition";
	}

	@Override
	public boolean isCompatibleWith(Label label) {
		return false;
	}

}
