package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import com.google.common.collect.Lists;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import de.unifrankfurt.faststring.analysis.label.StringTypeLabel;
import de.unifrankfurt.faststring.analysis.util.IRUtil;

public class ReceiverUse extends Use {

	private List<Integer> params;
	private MethodReference method;
	private int def;

	public ReceiverUse(final MethodReference method, int def, List<Integer> unfilteredParams) {
		this.method = method;
		this.def = def;
		this.params = Lists.newLinkedList();
		
		for (int i = 0; i < unfilteredParams.size(); i++) {
			
			TypeReference parameterType = method.getParameterType(i);
			if (IRUtil.STRING_TYPE.equals(parameterType)) {
				this.params.add(unfilteredParams.get(i));
			}
		}
	}

	@Override
	public String toString() {
		return "ReceiverUse [params=" + params + ", def=" + def + ", method=" + method + 
				", byteCodeIndex=" + getByteCodeIndex() + ", varIndex= " + getLocalVariableIndex()+ "]";
	}

	@Override
	public List<Integer> getConnectedRefs() {
		List<Integer> newRefs = Lists.newLinkedList(params);
		if (def > -1 && method.getReturnType().equals(IRUtil.STRING_TYPE)) {
			newRefs.add(def);
		}
		return newRefs;
	}

	@Override
	public boolean isCompatibleWith(StringTypeLabel label) {
		return label.canBeUsedAsReceiverFor(method);
	}

}
