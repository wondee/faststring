package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import com.ibm.wala.types.MethodReference;

public abstract class Definition extends DataFlowObject {


	public static Definition createParamDefinition(int i) {
		return new MethodParameterDefinition(i);
	}

	public static Definition createConstantDefinition(Object value) {
		return new ConstantDefinition(value);
	}

	public static Definition createCallResultDefinition(MethodReference method, int receiver) {
		return new CallResultDefinition(method, receiver);
	}

	public static Definition createPhiDefinition(List<Integer> refs) {
		return new PhiDefinition(refs);
	}


}
