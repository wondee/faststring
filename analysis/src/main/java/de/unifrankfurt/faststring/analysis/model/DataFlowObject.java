package de.unifrankfurt.faststring.analysis.model;

import java.util.Collection;
import java.util.List;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.util.IRUtil;

public abstract class DataFlowObject {

	private int byteCodeIndex = -1;
	
	private Collection<Integer> localVariableIndex = IRUtil.EMPTY_LIST;
	
	public List<Integer> getConnectedRefs(TypeLabel label) {
		return IRUtil.EMPTY_LIST;
	}
	
	public abstract boolean isCompatibleWith(TypeLabel label);
	
	public void setByteCodeIndex(int index) {
		this.byteCodeIndex = index;
	}
	
	public int getByteCodeIndex() {
		return byteCodeIndex;
	}

	public void setLocalVariableIndex(Collection<Integer> localVariableIndex) {
		this.localVariableIndex = localVariableIndex;
	}

	public Collection<Integer> getLocalVariableIndex() {
		return localVariableIndex;
	}
}
