package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public abstract class InstructionNode {
	private TypeLabel label;
	private int byteCodeIndex = -1;
	
	private Collection<Integer> localVariableIndex = ImmutableList.of();
	
	private Map<Integer, Collection<Integer>> localMap = Maps.newHashMap();
	
	public List<Integer> getConnectedRefs(TypeLabel label, int inV) {
		return ImmutableList.of();
	}
	
	public boolean isCompatibleWith(TypeLabel label, int inV) {
		if (label == null) {
			if (this.label == null) {
				return true;
			} else {
				return isCompatibleWithNullLabel(label, inV);
			}
		} else {
			if (this.label == null) {				
				return isCompatibleWithActual(label, inV);				
			} else {
				return this.label.compatibleWith(label);
			}
			
		}
	}
	
	protected boolean isCompatibleWithNullLabel(TypeLabel label, int inV) {
		return false;
	}

	protected boolean isCompatibleWithActual(TypeLabel label, int inV) {
		return this.label == label; 
	}
	
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
	
	public void setLabel(TypeLabel label) {
		this.label = label;
	}
	
	public TypeLabel getLabel() {
		return label;
	}
	
	public boolean isLabel(TypeLabel label) {
		return this.label == label;
	}

	public void addLocalVariableIndices(int v, Collection<Integer> locals) {
		assert localMap.put(v, locals) == null;
	}
}
