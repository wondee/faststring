package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;

import com.ibm.wala.types.TypeReference;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class NewNode extends InstructionNode {

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
	protected boolean isCompatibleWithActual(TypeLabel label, int inV) {
		return true;
	}
}
