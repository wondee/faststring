package de.unifrankfurt.faststring.analysis.label;

import static de.unifrankfurt.faststring.analysis.util.IRUtil.METHOD_STRING_VALUE_OF;
import static de.unifrankfurt.faststring.analysis.util.IRUtil.METHOD_SUBSTRING;
import static de.unifrankfurt.faststring.analysis.util.IRUtil.METHOD_SUBSTRING_DEFAULT_START;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.ibm.wala.types.MethodReference;

public class SubstringStringType extends BaseTypeLabel {

	public static final TypeLabel INSTANCE = new SubstringStringType();
	
	private static final ReceiverInfo RECEIVER_INFO = new ReceiverInfo(true, ImmutableList.<Integer>of());
	
	private static final Collection<MethodReference> methods = Sets.newHashSet(METHOD_SUBSTRING, METHOD_SUBSTRING_DEFAULT_START);
	
	@Override
	public Collection<MethodReference> methods() {
		return methods;
	}

	@Override
	public boolean canBeUsedAsParamFor(MethodReference method, int index) {
		// TODO check if its possible for stringbuilder
		Set<MethodReference> set = Sets.newHashSet(methods());
		set.add(METHOD_STRING_VALUE_OF);
		
		return set.contains(method);
	}

	@Override
	public boolean canBeUsedAsReceiverFor(MethodReference calledMethod) {
		return compatibleMethods().contains(calledMethod);
	}
	
	private Collection<MethodReference> compatibleMethods() {
		return methods();
	}

	@Override
	public boolean canBeDefinedAsResultOf(MethodReference method) {
		return methods().contains(method);
	}

	@Override
	public ReceiverInfo getReceiverUseInfo(MethodReference method) {
		if (canBeUsedAsReceiverFor(method)) {
			return RECEIVER_INFO;
		} else {
			return ReceiverInfo.NOT_USABLE_AS_RECEIVER;
		}
	}

	@Override
	public boolean canReturnedValueBeLabeled(MethodReference method) {
		return methods.contains(method);
	}
	
}
