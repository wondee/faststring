package de.unifrankfurt.faststring.analysis.model;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public class ConstantDefinition extends Definition {

	private Object value;

	public ConstantDefinition(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ConstantDefinition [value=" + value + "]";
	}

	@Override
	public boolean isCompatibleWith(TypeLabel label) {
		return false;
	}


	public Object getValue() {
		return value;
	}

}
