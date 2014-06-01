package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;

import de.unifrankfurt.faststring.analysis.util.IRUtil;

public abstract class DataFlowCreationObject {

	public List<Integer> getNewRefs() {
		return IRUtil.EMPTY_LIST;
	}
	
}
