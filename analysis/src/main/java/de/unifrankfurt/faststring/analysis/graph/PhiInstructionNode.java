package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ibm.wala.ssa.SSAPhiInstruction;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.util.IRUtil;

public class PhiInstructionNode extends InstructionNode {

	private List<Integer> uses;
	private int def;

	public PhiInstructionNode(SSAPhiInstruction instruction) {
		this(instruction.getDef(), IRUtil.getUses(instruction));
	}

	public PhiInstructionNode(int def, List<Integer> uses) {
		this.def = def;
		this.uses = uses;
	}

	@Override
	public String toString() {
		return "PhiNode [def=" + def + ", uses=" + uses + ", bcIndex=" + getByteCodeIndex() + ", localMap=" + localMap + "]";
	}

	@Override
	public List<Integer> getConnectedRefs(TypeLabel label, int inV) {
		List<Integer> refs = getConnctedRefs();
		refs.remove((Integer)inV);

		return refs;
	}

	public Collection<Integer> getConnectedRefs(int inV) {
		return getConnectedRefs(null, inV);
	}

	public List<Integer> getConnctedRefs() {
		List<Integer> refs = Lists.newLinkedList(uses);
		refs.add(def);
		return refs;
	}

	@Override
	public void visit(Visitor visitor) {
		visitor.visitPhi(this);
	}

}
