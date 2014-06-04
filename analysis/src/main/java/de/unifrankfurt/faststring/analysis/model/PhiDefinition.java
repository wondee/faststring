package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import de.unifrankfurt.faststring.analysis.label.Label;

public class PhiDefinition extends Definition {

	private List<Integer> refs;

	public PhiDefinition(List<Integer> refs) {
		this.refs = refs;
	}

	@Override
	public String toString() {
		return "PhiDefinition [refs=" + refs + "]";
	}

	@Override
	public List<Integer> getConnectedRefs() {
		return refs;
	}
	
	@Override
	public boolean isCompatibleWith(Label label) {
		// TODO need to check if the uses are all labeled with a compatible label
		return true;
	}
	
}
