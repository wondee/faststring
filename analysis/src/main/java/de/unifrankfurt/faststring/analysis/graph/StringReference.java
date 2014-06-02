package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.unifrankfurt.faststring.analysis.label.Label;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;


public class StringReference {

	private int ref;
	
	private Definition def = null;
	private List<Use> uses = Lists.newLinkedList();
	
	private Label label;

	public StringReference(int ref) {
		Preconditions.checkArgument(ref > 0, "valueNumber must be greater than 0");
		this.ref = ref;
		
	}


	public StringReference(int receiver, Label label) {
		this(receiver);
		this.label = label;
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
	
	public void setLabel(Label label) {
		this.label = label;
		
	}

	public Label getLabel() {
		return label;
	}

	public List<Use> getUses() {
		return uses;
	}

	public Integer getRef() {
		return ref;
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





}