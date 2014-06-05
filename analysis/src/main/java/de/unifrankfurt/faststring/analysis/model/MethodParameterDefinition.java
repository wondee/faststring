package de.unifrankfurt.faststring.analysis.model;

import de.unifrankfurt.faststring.analysis.label.StringTypeLabel;


public class MethodParameterDefinition extends Definition {

	@Override
	public String toString() {
		return "MethodParameterDefinition";
	}

	@Override
	public boolean isCompatibleWith(StringTypeLabel label) {
		return false;
	}

}
