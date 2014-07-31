package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;

import com.google.common.collect.Sets;


public class ParameterNode extends InstructionNode {

	private int index;

	public ParameterNode(int paramIndexFor) {
		this.index = paramIndexFor;
	}

	@Override
	public Collection<Integer> getLocals(Integer v) {
		return Sets.newHashSet(index);
	}

	@Override
	public String toString() {
		return "ParameterDefinition [index=" + index + "]";
	}

	@Override
	public void visit(Visitor visitor) {
		visitor.visitParameter(this);
	}


}
