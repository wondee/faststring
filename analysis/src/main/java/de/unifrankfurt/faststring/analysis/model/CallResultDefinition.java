package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import com.google.common.collect.Lists;
import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.analysis.label.Label;
import de.unifrankfurt.faststring.analysis.util.IRUtil;

public class CallResultDefinition extends Definition {

	
	private MethodReference method;
	private int receiver;

	public CallResultDefinition(MethodReference method, int receiver) {
		this.method = method;
		this.receiver = receiver;
	}

	@Override
	public List<Integer> getConnectedRefs() {
		if (method.getDeclaringClass().equals(IRUtil.STRING_TYPE)) {
			return Lists.newArrayList(receiver);
		} else {
			return IRUtil.EMPTY_LIST;
		}
	}

	@Override
	public boolean isCompatibleWith(Label label) {
		return label.canBeDefinedAsResultOf(method);
	}
	
	@Override
	public String toString() {
		return "CallResultDefinition [method=" + method + ", receiver="
				+ receiver + "]";
	}



	
}
