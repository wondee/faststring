package de.unifrankfurt.faststring.analysis.label;

import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.core.SubstringString;

public class MockLabel extends BaseTypeLabel {

	@Override
	public boolean canBeUsedAsParamFor(MethodReference method, int index) {
		return false;
	}

	@Override
	public boolean canBeUsedAsReceiverFor(MethodReference method) {
		return false;
	}

	@Override
	public boolean canBeDefinedAsResultOf(MethodReference method) {
		return false;
	}

	@Override
	public ReceiverInfo getReceiverUseInfo(MethodReference method) {
		return ReceiverInfo.NOT_USABLE_AS_RECEIVER;
	}

	@Override
	public boolean canReturnedValueBeLabeled(MethodReference method) {
		return false;
	}

	@Override
	protected Collection<MethodReference> methods() {
		return ImmutableList.of(SubstringStringType.METHOD_SUBSTRING, SubstringStringType.METHOD_SUBSTRING_DEFAULT_START);
	}

	@Override
	public Class<?> getOptimizedType() {
		return SubstringString.class;
	}

	@Override
	public Class<?> getOriginalType() {
		return String.class;
	}

	@Override
	public String getCreationMethodName() {
		return "valueOf";
	}

}
