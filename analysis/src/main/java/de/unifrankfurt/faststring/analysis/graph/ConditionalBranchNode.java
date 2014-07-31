package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;

import com.google.common.collect.Lists;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class ConditionalBranchNode extends LabelableNode {

	@Override
	public void visit(Visitor visitor) {
		visitor.visitBranch(this);

	}

	@Override
	public boolean canProduce(TypeLabel label) {
		return false;
	}
	
	@Override
	public List<Integer> getLabelableRefs(TypeLabel label) {
		return Lists.newLinkedList(getConnectedRefs());
	}

	@Override
	public boolean canUseAt(TypeLabel label, int i) {
		return true;
	}
}
