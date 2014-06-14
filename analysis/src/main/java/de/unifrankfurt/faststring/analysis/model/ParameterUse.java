package de.unifrankfurt.faststring.analysis.model;

import java.util.List;

import com.google.common.collect.Lists;
import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.analysis.label.StringTypeLabel;
import de.unifrankfurt.faststring.analysis.util.IRUtil;

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
	public List<Integer> getConnectedRefs() {
		// TODO check more precise in which cases the return value chould be labeled
		// in the actual error String.valueOf(labeledItem) does not return a labeled item but a normal String
		
		if (method.getReturnType().equals(IRUtil.STRING_TYPE)) {
			return Lists.newArrayList(def);			
		} else {
			return Lists.newArrayList();
		}
		
	}

	@Override
	public boolean isCompatibleWith(StringTypeLabel label) {
		return label.canBeUsedAsParamFor(method, index);
	}

}
