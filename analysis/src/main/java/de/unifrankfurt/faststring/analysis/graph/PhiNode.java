package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;

import com.google.common.collect.Lists;
import com.ibm.wala.ssa.SSAPhiInstruction;

import de.unifrankfurt.faststring.analysis.util.IRUtil;

public class PhiNode extends InstructionNode {


	public PhiNode(SSAPhiInstruction instruction) {
		this(instruction.getDef(), IRUtil.getUses(instruction));
	}

	public PhiNode(int def, List<Integer> uses) {
		setDef(def);
		setUses(uses);
	}

	@Override
	public String toString() {
		return "PhiNode [def=" + def + ", uses=" + uses + ", bcIndex=" + getByteCodeIndex() + ", localMap=" + localMap + "]";
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

	public List<Integer> getUses() {
		return uses;
	}

}
