package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.analysis.label.ReceiverInfo;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.util.IRUtil;


public class MethodCallNode extends InstructionNode {

	private int def;
	private List<Integer> uses;
	private MethodReference target;
	private boolean isStatic;

	private Map<Integer, TypeLabel> labels = Maps.newHashMap();
	private TypeLabel defLabel;

	public MethodCallNode(SSAInvokeInstruction instruction) {
		this(
			(instruction.hasDef()) ? instruction.getDef() : -1,
			IRUtil.getUses(instruction),
			instruction.getDeclaredTarget(),
			instruction.isStatic()
		);

	}

	public MethodCallNode(int def, List<Integer> uses,
			MethodReference target, boolean isStatic) {
		super();
		this.def = def;
		this.uses = uses;
		this.target = target;
		this.isStatic = isStatic;
	}

	public MethodReference getTarget() {
		return target;
	}

	private int getParam(int index) {
		return uses.get((isStatic) ? index - 1 : index);
	}

	public void addLabelToUse(Integer valueNumber, TypeLabel label) {
		labels.put(valueNumber, label);
	}

	@Override
	public List<Integer> getConnectedRefs(TypeLabel label, int inV) {
		return determineMethodCallType(inV).getConnectedRefs(label, inV);
	}

	@Override
	protected boolean isCompatibleWithActual(TypeLabel label, int inV) {
		return determineMethodCallType(inV).isCompatibleWithActual(label);

	}

	private MethodCallType determineMethodCallType(int inV) {
		if (inV == def) {
			return new Defintion();
		} else {

			int index = IRUtil.findUseIndex(inV, uses);

			if (index == 0 && !isStatic) {
				return new Receiver();
			} else {
				return new Parameter(index);
			}

		}
	}

	interface MethodCallType {
		boolean isCompatibleWithActual(TypeLabel label);

		List<Integer> getConnectedRefs(TypeLabel label, int inV);

	}

	private class Defintion implements MethodCallType {
		@Override
		public boolean isCompatibleWithActual(TypeLabel label) {
			return label.canBeDefinedAsResultOf(target);
		}
		@Override
		public List<Integer> getConnectedRefs(TypeLabel label, int inV) {
			// TODO only returns receiver, maybe other params may also be interesting
			if (label.canBeDefinedAsResultOf(target) && !isStatic) {

				return Lists.newArrayList(uses.get(0));
			} else {
				return ImmutableList.of();
			}
		}

	}

	private class Receiver implements MethodCallType {
		@Override
		public boolean isCompatibleWithActual(TypeLabel label) {
			return label.canBeUsedAsReceiverFor(target);
		}

		@Override
		public List<Integer> getConnectedRefs(TypeLabel label, int inV) {
			ReceiverInfo receiverInfo = label.getReceiverUseInfo(target);

			List<Integer> newRefs = Lists.newLinkedList();

			if (def != -1 && receiverInfo.isDefLabelable()) {
				newRefs.add(def);
			}

			for (Integer index : receiverInfo.getLabelableParams()) {
				newRefs.add(getParam(index));
			}

			return newRefs;
		}

	}

	private class Parameter implements MethodCallType {

		private int index;

		public Parameter(int index) {
			this.index = index;
		}

		@Override
		public boolean isCompatibleWithActual(TypeLabel label) {
			return label.canBeUsedAsParamFor(target, index);
		}

		@Override
		public List<Integer> getConnectedRefs(TypeLabel label, int inV) {
			if (label.canReturnedValueBeLabeled(target)) {
				return Lists.newArrayList(def);
			} else {
				return Lists.newArrayList();
			}
		}

	}

	@Override
	public String toString() {
		return "MethodCallNode [def=" + def + ", uses=" + uses
				+ ", target=" + target + ", isStatic=" + isStatic + ", bcIndex=" +
				getByteCodeIndex() + " localVarIndex=" + localMap + ", loadMap=" + loadMap + ", storeMap=" + storeMap + " ]";
	}

	@Override
	public void visit(Visitor visitor) {
		visitor.visitMethodCall(this);
	}

	public boolean isReceiver(int v) {
		return determineMethodCallType(v) instanceof Receiver;
	}

	public Collection<Integer> getDefLocals() {
		return getLocals(def);
	}

	public List<Integer> getParams() {
		return uses.subList((isStatic) ? 0 : 1, uses.size());
	}

	public TypeLabel getLabelForUse(int v) {
		return labels.get(v);
	}

	public TypeLabel getDefLabel() {
		return defLabel;
	}

	public void setDefLabel(TypeLabel label) {
		defLabel = label;

	}

}
