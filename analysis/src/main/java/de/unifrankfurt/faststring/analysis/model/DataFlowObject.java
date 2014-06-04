package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import de.unifrankfurt.faststring.analysis.label.Label;
import de.unifrankfurt.faststring.analysis.util.IRUtil;

public abstract class DataFlowObject {

	public List<Integer> getConnectedRefs() {
		return IRUtil.EMPTY_LIST;
	}
	
	public abstract boolean isCompatibleWith(Label label);
}
