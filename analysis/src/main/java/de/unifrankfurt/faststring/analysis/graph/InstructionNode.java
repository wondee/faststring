package de.unifrankfurt.faststring.analysis.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public abstract class InstructionNode implements Labelable {

	private static final Logger LOG = LoggerFactory
			.getLogger(InstructionNode.class);

	private TypeLabel label;
	private int byteCodeIndex = -1;

	Map<Integer, Collection<Integer>> localMap = Maps.newHashMap();

	/** maps from use index to bcIndex where this value is pushed to the stack */
	Map<Integer, Integer> loadMap = Maps.newHashMap();

	int storeIndex = -1;
	int def = -1;
	List<Integer> uses;

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
		Collection<Integer> locals = localMap.get(v);
		if (locals == null) {
			locals = Sets.newHashSet();
		}

		return locals;
	}

	public void addLocalVariableIndices(int v, Collection<Integer> locals) {
		if (!localMap.containsKey(v)) {
			localMap.put(v, locals);
		} else {
			localMap.get(v).addAll(locals);
		}
	}

	public Integer getLoad(int i) {
		return loadMap.get(i);
	}

	public Integer getStore() {
		return storeIndex;
	}

	public void setStore(int local, int bcIndex) {
		int def = getDef();
		if (def != -1) {
			addLocalVariableIndices(def, Arrays.asList(local));

			if (storeIndex == -1) {
				storeIndex = bcIndex;
			} else {
				throw new IllegalStateException(String.format("store was set twice: old=%d; new=%d", storeIndex, bcIndex));
			}
		} else {
			throw new IllegalStateException(String.format("instruction %s has no def but a store at %d for local %d",
					this, bcIndex, local));
		}

	}

	public Iterable<Integer> getIndicesForV(Integer valueNumber) {

		List<Integer> indices = Lists.newLinkedList();

		for (int i = 0; i < uses.size(); i++) {
			if (uses.get(i) == valueNumber) {
				indices.add(i);
			}
		}

		return indices;
	}


	void setDef(int def) {
		this.def = def;
	}

	void setUses(List<Integer> uses) {
		this.uses = uses;
	}

	public int getDef() {
		return def;
	}

	public void addLoad(int index, int load) {
		LOG.trace("adding load {}, for index {}", load, index);
		Integer old = loadMap.put(index, load);

		if (old != null && old != load) {
			throw new IllegalStateException(String.format("old value was removed from loadMap index %index old %d new %d", index, old, load));
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

	public boolean isCompatibleWith(Reference ref) {
		return isCompatibleWith(ref.getLabel(), ref.valueNumber());
	}

	public static class Visitor {

		public void visitConstant(ConstantNode node) {}

		public void visitMethodCall(MethodCallNode node) {}

		public void visitParameter(ParameterNode node) {}

		public void visitPhi(PhiNode node) {}

		public void visitReturn(ReturnNode node) {}

		public void visitNew(NewNode newNode) {}

		public void visitGet(GetNode getNode) {}

		public void visitBranch(ConditionalBranchNode conditionalBranchNode) {}

	}


}
