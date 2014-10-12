package de.unifrankfurt.faststring.analysis.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class InstructionNode {

	private static final Logger LOG = LoggerFactory
			.getLogger(InstructionNode.class);


	private int byteCodeIndex = -1;

	Map<Integer, Collection<Integer>> localMap = Maps.newHashMap();

	/** maps from use index to bcIndex where this value is pushed to the stack */
	Map<Integer, Integer> loadMap = Maps.newHashMap();

	int storeIndex = -1;
	int def = -1;
	List<Integer> uses = ImmutableList.of();

	public final List<Integer> getConnectedRefs(int inV) {
		Set<Integer> refs = getConnectedRefs();

		refs.remove(inV);

		return ImmutableList.copyOf(refs);
	}


	public Set<Integer> getConnectedRefs() {
		Set<Integer> refs = Sets.newHashSet(uses);

		if (def != -1) {
			refs.add(def);
		}
		return refs;
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
			if (local != -1) {
				addLocalVariableIndices(def, Arrays.asList(local));
			}
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

	public Iterable<Integer> getIndicesForV(int valueNumber) {

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

	public List<Integer> getUses() {
		return uses;
	}

	public void addLoad(int index, int load) {
		LOG.trace("adding load {}, for index {}", load, index);
		Integer old = loadMap.put(index, load);

		if (old != null && old != load) {
			throw new IllegalStateException(String.format("old value was removed from loadMap index %index old %d new %d", index, old, load));
		}
	}

	public abstract void visit(Visitor visitor);


	public static class Visitor {

		public void visitConstant(ConstantNode node) {}

		public void visitMethodCall(MethodCallNode node) {}

		public void visitParameter(ParameterNode node) {}

		public void visitPhi(PhiNode node) {}

		public void visitReturn(ReturnNode node) {}

		public void visitNew(NewNode newNode) {}

		public void visitGet(GetNode getNode) {}

		public void visitBranch(ConditionalBranchNode conditionalBranchNode) {}

		public void visitBinaryOperator(BinaryOperation binaryOperation) {}

	}

	public static class MustCallVisitor extends Visitor {
		public void visitConstant(ConstantNode node) { throw new UnsupportedOperationException(); }

		public void visitMethodCall(MethodCallNode node) { throw new UnsupportedOperationException(); }

		public void visitParameter(ParameterNode node) { throw new UnsupportedOperationException(); }

		public void visitPhi(PhiNode node) { throw new UnsupportedOperationException(); }

		public void visitReturn(ReturnNode node) { throw new UnsupportedOperationException(); }

		public void visitNew(NewNode newNode) { throw new UnsupportedOperationException(); }

		public void visitGet(GetNode getNode) { throw new UnsupportedOperationException(); }

		public void visitBranch(ConditionalBranchNode conditionalBranchNode) { throw new UnsupportedOperationException(); }

		public void visitBinaryOperator(BinaryOperation binaryOperation) { throw new UnsupportedOperationException(); }
	}

}
