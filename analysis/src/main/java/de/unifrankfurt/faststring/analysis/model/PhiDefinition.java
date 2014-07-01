package de.unifrankfurt.faststring.analysis.model;

import java.util.Collection;
import java.util.List;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class PhiDefinition extends Definition implements PhiObject{

	private List<Integer> refs;
	private TypeLabel label;

	public PhiDefinition(List<Integer> refs) {
		this.refs = refs;
	}

	@Override
	public String toString() {
		return "PhiDefinition [refs=" + refs + "]";
	}

	@Override
	public List<Integer> getConnectedRefs(TypeLabel label) {
		return refs;
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
		result = prime * result + ((refs == null) ? 0 : refs.hashCode());
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
		PhiDefinition other = (PhiDefinition) obj;
		if (refs == null) {
			if (other.refs != null)
				return false;
		} else if (!refs.equals(other.refs))
			return false;
		return true;
	}
	
}
