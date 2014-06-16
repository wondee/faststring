package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import com.google.common.collect.Lists;
import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.analysis.label.ReceiverInfo;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class ReceiverUse extends Use {

	private List<Integer> params;
	private MethodReference method;
	private int def;

	public ReceiverUse(final MethodReference method, int def, List<Integer> params) {
		this.method = method;
		this.def = def;
		this.params = params;
	}

	@Override
	public String toString() {
		return "ReceiverUse [params=" + params + ", def=" + def + ", method=" + method + 
				", byteCodeIndex=" + getByteCodeIndex() + ", varIndex= " + getLocalVariableIndex()+ "]";
	}

	@Override
	public List<Integer> getConnectedRefs(TypeLabel label) {
		ReceiverInfo receiverInfo = label.getReceiverUseInfo(method);
		
		List<Integer> newRefs = Lists.newLinkedList();
		
		if (def != -1 && receiverInfo.isDefLabelable()) {
			newRefs.add(def);
		}
		
		for (Integer index : receiverInfo.getLabelableParams()) {
			newRefs.add(params.get(index));
		}
		
		return newRefs;
	}

	@Override
	public boolean isCompatibleWith(TypeLabel label) {
		return label.canBeUsedAsReceiverFor(method);
	}

}
