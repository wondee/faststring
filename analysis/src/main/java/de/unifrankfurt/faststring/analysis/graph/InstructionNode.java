package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public abstract class InstructionNode implements Labelable {
	private TypeLabel label;
	private int byteCodeIndex = -1;

	Map<Integer, Collection<Integer>> localMap = Maps.newHashMap();
	Map<Integer, Integer> loadMap = Maps.newHashMap();

	public List<Integer> getConnectedRefs(TypeLabel label, int inV) {
		return ImmutableList.of();
	}

	public boolean isCompatibleWith(TypeLabel label, int inV) {
		if (label == null) {
			if (this.label == null) {
				return true;
			} else {
				return isCompatibleWithNullLabel(label, inV);
			}
		} else {
			if (this.label == null) {
				return isCompatibleWithActual(label, inV);
			} else {
				return this.label.compatibleWith(label);
			}

		}
	}

	protected boolean isCompatibleWithNullLabel(TypeLabel label, int inV) {
		return false;
	}

	protected boolean isCompatibleWithActual(TypeLabel label, int inV) {
		return this.label == label;
	}

	public void setByteCodeIndex(int index) {
		this.byteCodeIndex = index;
	}

	public int getByteCodeIndex() {
		return byteCodeIndex;
	}

	public Collection<Integer> getLocals(Integer v) {
		return localMap.get(v);
	}

	public void addLocalVariableIndices(int v, Collection<Integer> locals) {
		if (!localMap.containsKey(v)) {
			localMap.put(v, locals);
		} else {
			localMap.get(v).addAll(locals);
		}

	}

	public void addLoad(Integer local, int load) {
		Integer old = loadMap.put(local, load);

		if (old != null && old != load) {
			throw new IllegalStateException(String.format("old value was removed from loadMap local %d old %d new %d", local, old, load));
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.unifrankfurt.faststring.analysis.graph.Labelable#setLabel(de.unifrankfurt
	 * .faststring.analysis.label.TypeLabel)
	 */
	@Override
	public void setLabel(TypeLabel label) {
		this.label = label;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.unifrankfurt.faststring.analysis.graph.Labelable#getLabel()
	 */
	@Override
	public TypeLabel getLabel() {
		return label;
	}

	public abstract void visit(Visitor visitor);

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.unifrankfurt.faststring.analysis.graph.Labelable#isLabel(de.unifrankfurt
	 * .faststring.analysis.label.TypeLabel)
	 */
	@Override
	public boolean isLabel(TypeLabel label) {
		return this.label == label;
	}

	@Override
	public boolean isSameLabel(Labelable other) {
		return isLabel(other.getLabel());
	}

	public static class Visitor {

		public void visitConstant(ConstantNode node) {}

		public void visitMethodCall(MethodCallNode node) {}

		public void visitParameter(ParameterNode node) {}

		public void visitPhi(PhiNode node) {}

		public void visitReturn(ReturnNode node) {}

	}

}
