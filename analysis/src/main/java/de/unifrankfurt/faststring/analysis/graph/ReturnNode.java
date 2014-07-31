package de.unifrankfurt.faststring.analysis.graph;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Preconditions;

public class ReturnNode extends NotLabelableNode {

	public ReturnNode(int result) {
		Preconditions.checkArgument(result > 0);
		setUses(Arrays.asList(result));
	}

	@Override
	public String toString() {
		return "ReturnInstruction [result=" + uses.get(0) + ", getByteCodeIndex()="
				+ getByteCodeIndex() + ", getLocalVariableIndex()="
				+ getLocals() + "]";
	}

	public Collection<Integer> getLocals() {
		return getLocals(uses.get(0));
	}

	@Override
	public void visit(Visitor visitor) {
		visitor.visitReturn(this);
	}

}
