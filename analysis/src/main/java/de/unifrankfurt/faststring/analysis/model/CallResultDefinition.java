package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class CallResultDefinition extends Definition {

	
	private MethodReference method;
	private int receiver;

	public CallResultDefinition(MethodReference method, int receiver) {
		this.method = method;
		this.receiver = receiver;
	}

	@Override
	public List<Integer> getConnectedRefs(TypeLabel label) {
		if (label.canBeDefinedAsResultOf(method)) {
		
//		if (method.getDeclaringClass().equals(IRUtil.STRING_TYPE)) {
			return Lists.newArrayList(receiver);
		} else {
			return ImmutableList.of();
		}
	}

	@Override
	public boolean isCompatibleWith(TypeLabel label) {
		return label.canBeDefinedAsResultOf(method);
	}
	
	@Override
	public String toString() {
		return "CallResultDefinition [method=" + method + ", receiver="
				+ receiver + ", byteCodeIndex=" + getByteCodeIndex() + ", varIndex= " + getLocalVariableIndex()+ "]";
	}



	
}
