package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.ibm.wala.types.TypeReference;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class NewNode extends LabelableNode {

	private TypeReference type;
	private int ctorCallIndex;

	public NewNode(int def, TypeReference concreteType) {
		this.type = concreteType;
		this.def = def;
	}

	public void setCtorCallIndex(int ctorCallIndex) {
		this.ctorCallIndex = ctorCallIndex;
	}

	@Override
	public void visit(Visitor visitor) {
		visitor.visitNew(this);
	}

	@Override
	public String toString() {
		return "NewNode [def=" + def + ", type=" + type + ",ctorCall=" + ctorCallIndex + " localMap=" + localMap
				+ " storeIndex=" + storeIndex + ", getByteCodeIndex()=" + getByteCodeIndex() + "]";
	}

	public Collection<Integer> getDefLocal() {
		return getLocals(def);
	}

	public TypeReference getType() {
		return type;
	}

	@Override
	public boolean canProduce(TypeLabel label) {
		return true;
	}

	@Override
	public List<Integer> getLabelableRefs(TypeLabel label) {
		return ImmutableList.of();
	}

	@Override
	public boolean canUseAt(TypeLabel label, int i) {
		return true;
	}
}
