package de.unifrankfurt.faststring.analysis.model;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public class ConstantDefinition<T> extends Definition {

	private T value;

	public ConstantDefinition(T value) {
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




}
