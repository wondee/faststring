package de.unifrankfurt.faststring.analysis.model;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public abstract class DataFlowObject {

	private int byteCodeIndex = -1;
	
	private Collection<Integer> localVariableIndex = ImmutableList.of();
	
	public List<Integer> getConnectedRefs(TypeLabel label) {
		return ImmutableList.of();
	}
	
	public abstract boolean isCompatibleWith(TypeLabel label);
	
	public void setByteCodeIndex(int index) {
		this.byteCodeIndex = index;
	}
	
	public int getByteCodeIndex() {
		return byteCodeIndex;
	}

	public void setLocalVariableIndices(Collection<Integer> localVariableIndex) {
		this.localVariableIndex = ImmutableSet.copyOf(localVariableIndex);
	}

	public Collection<Integer> getLocalVariableIndex() {
		return localVariableIndex;
	}
}
