package de.unifrankfurt.faststring.analysis.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public class Reference {

	private int v;

	private InstructionNode definition = null;

	private List<InstructionNode> uses = Lists.newLinkedList();

	private TypeLabel label = null;

	private boolean definitionConversionToOpt = false;
	private boolean definitionConversionFromOpt = false;
	
	private Set<Integer> useConversionsToOpt = Sets.newHashSet();
	private Set<Integer> useConversionsFromOpt = Sets.newHashSet();

	public Reference(int ref) {
		Preconditions.checkArgument(ref > 0, "valueNumber must be greater than 0");
		this.v = ref;

	}


	public Reference(int receiver, TypeLabel label) {
		this(receiver);
		this.label = label;
	}


	public int valueNumber() {
		return v;
	}

	public void setDefinition(InstructionNode instructionNode) {
		this.definition = instructionNode;
	}

	public InstructionNode getDefinition() {
		return definition;
	}
	

	public void setLabel(TypeLabel label) {
		this.label = label;

	}

	public TypeLabel getLabel() {
		return label;
	}

	public List<InstructionNode> getUses() {
		return uses;
	}

	public Integer getRef() {
		return v;
	}

	public boolean isDefinitionConversionToOpt() {
		return definitionConversionToOpt;
	}

	public boolean isDefinitionConversionFromOpt() {
		return definitionConversionFromOpt;
	}

	public Set<Integer> getUseConversionsFromOpt() {
		return useConversionsFromOpt;
	}

	public Set<Integer> getUseConversionsToOpt() {
		return useConversionsToOpt;
	}
	
	void setUses(List<InstructionNode> uses) {
		this.uses = ImmutableList.copyOf(uses);
	}


	public void createBarriers() {
		checkDefinitionBarrier();
		checkUseBarriers();
		
	}
	
	private void checkDefinitionBarrier() {
		if (!definition.isLabel(label)) {
			if (label == null) {
				definitionConversionFromOpt = true;
			} else {
				definitionConversionToOpt = true;
			}
		}
	}
	
	private void checkUseBarriers() {
		for (int i = 0; i < uses.size(); i++) {
			InstructionNode use = uses.get(i);
			
			if (!use.isLabel(label)) {
				if (label == null) {
					useConversionsToOpt.add(i);
				} else {
					useConversionsFromOpt.add(i);
				}
			}
		}
	}
	

	void setUsesMutable(LinkedList<InstructionNode> uses) {
		this.uses = uses;
	}


	public List<Integer> getConnectedRefs(final TypeLabel label) {
		List<Integer> refs = Lists.newLinkedList(definition.getConnectedRefs(label, v));
		
		Iterables.<Integer>addAll(refs, 
				Iterables.concat(Iterables.transform(uses, new Function<InstructionNode, List<Integer>>() {
						@Override
						public List<Integer> apply(InstructionNode input) {
							return input.getConnectedRefs(label, v);
						}
					}
				))
			);
		
		return refs;
	}
	
	@Override
	public String toString() {
		return "Reference [v=" + v + ", def=" + definition
				+ ", uses=" + uses + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + v;
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
		if (v != other.v)
			return false;
		return true;
	}










}