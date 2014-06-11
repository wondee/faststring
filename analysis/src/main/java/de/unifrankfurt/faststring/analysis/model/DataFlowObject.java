package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import de.unifrankfurt.faststring.analysis.label.StringTypeLabel;
import de.unifrankfurt.faststring.analysis.util.IRUtil;

public abstract class DataFlowObject {

	private int index = -1;
	
	public List<Integer> getConnectedRefs() {
		return IRUtil.EMPTY_LIST;
	}
	
	public abstract boolean isCompatibleWith(StringTypeLabel label);
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
}
