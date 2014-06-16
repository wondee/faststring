package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import com.google.common.collect.Lists;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

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
	public List<Integer> getConnectedRefs(TypeLabel label) {
		return Lists.newArrayList(def);
	}

	@Override
	public boolean isCompatibleWith(TypeLabel label) {
		return true;
	}

}
