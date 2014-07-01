package de.unifrankfurt.faststring.analysis.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class PhiUse extends Use implements PhiObject {

	private int def;
	private Collection<Integer> otherUses;
	private TypeLabel label;

	public PhiUse(int def, Collection<Integer> otherUses) {
		this.def = def;
		this.otherUses = otherUses;
	}

	@Override
	public String toString() {
		return "PhiUse [def=" + def + ", otherUses=" + otherUses + "]";
	}

	@Override
	public List<Integer> getConnectedRefs(TypeLabel label) {
		LinkedList<Integer> list = Lists.newLinkedList(otherUses);
		list.add(def);
		
		return list;
	}

	@Override
	public boolean isCompatibleWith(TypeLabel label) {
		return label == this.label;
	}

	@Override
	public void setLabel(TypeLabel label) {
		this.label = label;
	}

	@Override
	public TypeLabel getLabel() {
		return label;
	}

	@Override
	public Collection<Integer> getConnctedRefs() {
		return getConnectedRefs(null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + def;
		result = prime * result
				+ ((otherUses == null) ? 0 : otherUses.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhiUse other = (PhiUse) obj;
		if (def != other.def)
			return false;
		if (otherUses == null) {
			if (other.otherUses != null)
				return false;
		} else if (!otherUses.equals(other.otherUses))
			return false;
		return true;
	}
	
	
}
