package de.unifrankfurt.faststring.core.label;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import de.unifrankfurt.faststring.analysis.label.BaseTypeLabel;
import de.unifrankfurt.faststring.analysis.label.ReceiverInfo;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.core.SubstringString;

public class SubstringStringType extends BaseTypeLabel {

	public static final TypeReference TYPE_STRING = TypeReference.findOrCreate(
			ClassLoaderReference.Application, "Ljava/lang/String");

	public static final MethodReference METHOD_SUBSTRING = MethodReference
			.findOrCreate(TYPE_STRING, "substring", "(II)Ljava/lang/String;");

	public static final MethodReference METHOD_SUBSTRING_DEFAULT_START = MethodReference
			.findOrCreate(TYPE_STRING, "substring", "(I)Ljava/lang/String;");

	public static final MethodReference METHOD_STRING_VALUE_OF = MethodReference
			.findOrCreate(TYPE_STRING, "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;");

	// optimized types and methods

//	public static final TypeReference TYPE_SUBSTRING_STRING = TypeReference.findOrCreate(
//			ClassLoaderReference.Application, "Lde/unifrankfurt/faststring/core/SubstringString");
//
//	public static final MethodReference METHOD_SUBSTRING_STRING_VALUE_OF = MethodReference
//			.findOrCreate(TYPE_STRING, "valueOf", "(Ljava/lang/String;)Lde/unifrankfurt/faststring/core/SubstringString;");

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

	@Override
	public Class<?> getOptimizedType() {
		return SubstringString.class;
	}
	
	@Override
	public String getCreationMethodName() {
		return "valueOf";
	}

	@Override
	public String toString() {
		return "SUBSTRING";
	}

	@Override
	public boolean compatibleWith(TypeLabel label) {
		return this.equals(label);
	}

	@Override
	public Class<?> getOriginalType() {
		return String.class;
	}

	@Override
	public String getToOriginalMethodName() {
		return "toString";
	}
}
