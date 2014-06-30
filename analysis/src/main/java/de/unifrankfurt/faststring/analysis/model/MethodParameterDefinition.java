package de.unifrankfurt.faststring.analysis.model;

import com.google.common.collect.ImmutableList;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public class MethodParameterDefinition extends Definition {


	public MethodParameterDefinition(int i) {
		setLocalVariableIndices(ImmutableList.of(i));
	}

	@Override
	public String toString() {
		return "MethodParameterDefinition [localIndex=" + getLocalVariableIndex() + "]";
	}

	@Override
	public boolean isCompatibleWith(TypeLabel label) {
		return false;
	}

}
