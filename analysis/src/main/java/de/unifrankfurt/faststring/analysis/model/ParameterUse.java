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
		return "ParamUse [method=" + method + ", def=" + def + ", index=" + index + "]";
	}

	@Override
	public List<Integer> getConnectedRefs() {
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
