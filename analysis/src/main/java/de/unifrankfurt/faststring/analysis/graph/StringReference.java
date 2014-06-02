package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;


public class StringReference {

	protected int ref;
	
	protected Definition def = null;
	protected List<Use> uses = Lists.newLinkedList();
	

	public StringReference(int ref) {
		Preconditions.checkArgument(ref > 0, "valueNumber must be greater than 0");
		this.ref = ref;
	}


	public int valueNumber() {
		return ref;
	}

	public void addUse(Use useInfo) {
		uses.add(useInfo);
		
	}


	public void setDefinition(Definition def) {
		if (this.def != null) {
			throw new IllegalStateException("def was tried to set twice");
		}
		
		this.def = def;
	}

	public Definition getDef() {
		return def;
	}

	@Override
	public String toString() {
		return "StringReference [ref=" + ref + ", def=" + def
				+ ", uses=" + uses + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ref;
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
		StringReference other = (StringReference) obj;
		if (ref != other.ref)
			return false;
		return true;
	}


	public List<Use> getUses() {
		return uses;
	}

}