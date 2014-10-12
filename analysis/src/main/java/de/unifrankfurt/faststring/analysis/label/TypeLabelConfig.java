package de.unifrankfurt.faststring.analysis.label;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.ibm.wala.shrikeBT.Util;
import com.ibm.wala.types.MethodReference;

public class TypeLabelConfig extends BaseTypeLabel {

	private String name;
	private Class<?> originalType;
	private Class<?> optimizedType;

	private Set<MethodReference> effectedMethods;
	private Set<MethodReference> supportedMethods;

	private Set<MethodReference> producingMethods;

	private Map<MethodReference, String> paramDifference;

	private Map<MethodReference, Set<Integer>> paramUsage;

	private String toOrignalMethodName;
	private String staticFactory;

	private Set<String> compatibleLabels;

	@Override
	public boolean canBeUsedAsParamFor(MethodReference method, int index) {
		if (paramUsage.containsKey(method)) {
			return paramUsage.get(method).contains(index);
		}

		return false;
	}

	@Override
	public boolean canBeUsedAsReceiverFor(MethodReference method) {
		return Sets.union(effectedMethods, supportedMethods).contains(method);
	}

	@Override
	public boolean canBeDefinedAsResultOf(MethodReference method) {
		return producingMethods.contains(method);
	}


	@Override
	public Class<?> getOptimizedType() {
		return optimizedType;
	}

	@Override
	public String getCreationMethodName() {
		return staticFactory;
	}

	@Override
	public boolean compatibleWith(TypeLabel label) {
		if (label == null) return false;

		return compatibleLabels.contains(label.toString());
	}

	@Override
	public Class<?> getOriginalType() {
		return originalType;
	}

	@Override
	public String getToOriginalMethodName() {
		return toOrignalMethodName;
	}

	@Override
	public String getReturnType(MethodReference target) {
		if (producingMethods.contains(target)) {
			return Util.makeType(optimizedType);
		} else {
			return target.getReturnType().getName().toString();
		}
	}

	@Override
	public String getParams(MethodReference target) {
		if (paramDifference.containsKey(target)) {
			return paramDifference.get(target);
		}
		return null;
	}

	@Override
	protected Collection<MethodReference> methods() {
		return effectedMethods;
	}

	@Override
	public String toString() {
		return name;
	}

}
