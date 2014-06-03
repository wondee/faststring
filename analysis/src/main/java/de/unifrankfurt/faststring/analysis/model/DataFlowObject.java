package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import de.unifrankfurt.faststring.analysis.util.IRUtil;

public abstract class DataFlowObject {

	public List<Integer> getConnectedRefs() {
		return IRUtil.EMPTY_LIST;
	}
	
}
