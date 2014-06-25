package de.unifrankfurt.faststring.analysis.model;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public class MethodParameterDefinition extends Definition {

	private final List<Integer> localIndex;
	
	public MethodParameterDefinition(int i) {
		this.localIndex = ImmutableList.of(i);
	}

	@Override
	public String toString() {
		return "MethodParameterDefinition [localIndex=" + localIndex.get(0) + "]";
	}

	@Override
	public boolean isCompatibleWith(TypeLabel label) {
		return false;
	}
	
	@Override
	public Collection<Integer> getLocalVariableIndex() {
		return localIndex;
	}

}
