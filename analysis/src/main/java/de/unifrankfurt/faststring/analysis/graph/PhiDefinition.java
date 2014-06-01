package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;

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
	public List<Integer> getNewRefs() {
		return refs;
	}
	
}
