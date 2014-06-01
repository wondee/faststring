package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;

import com.google.common.collect.Lists;
import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.analysis.util.IRUtil;

public class ParamUse extends Use {

	private MethodReference method;
	private int def;
	private int index;

	public ParamUse(MethodReference method, int def, int index) {
		this.method = method;
		this.def = def;
		this.index = index;
	}

	@Override
	public String toString() {
		return "ParamUse [method=" + method + ", def=" + def + ", index=" + index + "]";
	}

	@Override
	public List<Integer> getNewRefs() {
		if (method.getReturnType().equals(IRUtil.STRING_TYPE)) {
			return Lists.newArrayList(def);			
		} else {
			return Lists.newArrayList();
		}
		
	}

}
