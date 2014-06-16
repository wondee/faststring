package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;


public class Reference {

	private int ref;
	
	private Definition def = null;
	private List<Use> uses = null;
	
	private TypeLabel label = null;

	private boolean definitionConversion = false;
	private Set<Integer> useConversions = Sets.newHashSet();

	public Reference(int ref) {
		Preconditions.checkArgument(ref > 0, "valueNumber must be greater than 0");
		this.ref = ref;
		
	}


	public Reference(int receiver, TypeLabel label) {
		this(receiver);
		this.label = label;
	}


	public int valueNumber() {
		return ref;
	}

	void setDefinition(Definition def) {
		if (this.def != null) {
			throw new IllegalStateException("def was tried to set twice");
		}
		
		this.def = def;
	}

	public Definition getDef() {
		return def;
	}
	
	public void setLabel(TypeLabel label) {
		this.label = label;
		
	}

	public TypeLabel getLabel() {
		return label;
	}

	public List<Use> getUses() {
		return uses;
	}

	public Integer getRef() {
		return ref;
	}

	public void setConvertToDefinition() {
		definitionConversion = true;
	}

	public boolean isDefinitionConversion() {
		return definitionConversion;
	}
	
	public void setConvertToUse(Integer useId) {
		useConversions.add(useId);
	}

	public Set<Integer> getUseConversions() {
		return useConversions;
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
		Reference other = (Reference) obj;
		if (ref != other.ref)
			return false;
		return true;
	}


	void setUses(List<Use> uses) {
		if (this.uses != null) {
			throw new IllegalStateException("uses was tried to set twice");
		}
		this.uses = uses;
	}





}