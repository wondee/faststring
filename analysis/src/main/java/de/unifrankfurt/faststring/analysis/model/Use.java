package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import com.ibm.wala.types.MethodReference;

public abstract class Use extends DataFlowObject {

	public static Use createPassedAsParam(MethodReference method, int def, int index) {
		return new ParameterUse(method, def, index);
	}
	
	public static Use createReturned() {
		return new ReturnUse();
	}


	public static Use createUsedAsReceiver(MethodReference method, int def, List<Integer> params) {
		return new ReceiverUse(method, def, params);
	}

	public static Use createUsedInPhi(int def) {
		return new PhiUse(def);
	}

	
}
