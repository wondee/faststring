package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import com.google.common.collect.Lists;

public class PhiUse extends Use {

	private int def;

	public PhiUse(int def) {
		this.def = def;
	}

	@Override
	public String toString() {
		return "PhiUse [def=" + def + "]";
	}

	@Override
	public List<Integer> getNewRefs() {
		return Lists.newArrayList(def);
	}

}
