package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import com.google.common.collect.Lists;
import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class ParameterUse extends Use {

	private MethodReference method;
	private int def;
	private int index;

	public ParameterUse(MethodReference method, int def, int index) {
		this.method = method;
		this.def = def;
		this.index = index;
	}

	@Override
	public String toString() {
		return "ParameterUse [method=" + method + ", def=" + def + ", index=" + index + 
				", byteCodeIndex=" + getByteCodeIndex() + ", varIndex= " + getLocalVariableIndex()+ "]";
	}

	@Override
	public List<Integer> getConnectedRefs(TypeLabel label) {
		if (label.canReturnedValueBeLabeled(method)) {
			return Lists.newArrayList(def);			
		} else {
			return Lists.newArrayList();
		}
		
	}

	@Override
	public boolean isCompatibleWith(TypeLabel label) {
		return label.canBeUsedAsParamFor(method, index);
	}

}
