package de.unifrankfurt.faststring.analysis.model;

import de.unifrankfurt.faststring.analysis.label.Label;


public class ConstantDefinition extends Definition {

	
	@Override
	public String toString() {
		return "ConstantDefinition";
	}

	@Override
	public boolean isCompatibleWith(Label label) {
		return false;
	}




}
