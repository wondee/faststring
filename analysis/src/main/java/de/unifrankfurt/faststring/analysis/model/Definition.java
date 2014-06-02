package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.analysis.graph.DataFlowCreationObject;

public abstract class Definition extends DataFlowCreationObject {

	
	public static Definition createParamDefinition() {
		return new ParameterDefinition();
	}
	
	public static Definition createConstantDefinition() {
		return new ConstantDefinition();
	}
	
	public static Definition createCallResultDefinition(MethodReference method, int receiver) {
		return new CallResultDefinition(method, receiver);
	}
	
	public static Definition createPhiDefinitionInfo(List<Integer> refs) {
		return new PhiDefinition(refs);
	}

	
}
