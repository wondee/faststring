package de.unifrankfurt.faststring.core.label;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import de.unifrankfurt.faststring.analysis.label.BaseTypeLabel;
import de.unifrankfurt.faststring.analysis.label.ReceiverInfo;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.core.StringListBuilder;
import de.unifrankfurt.faststring.core.SubstringString;

public class StringListBuilderType extends BaseTypeLabel {

	public static final TypeReference TYPE_STRINGBUILDER = TypeReference.findOrCreate(
			ClassLoaderReference.Application, "Ljava/lang/StringBuilder");

	// TODO add init method

	public static final MethodReference METHOD_APPEND = MethodReference
			.findOrCreate(TYPE_STRINGBUILDER, "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");

	public static final MethodReference METHOD_TOSTRING = MethodReference
			.findOrCreate(TYPE_STRINGBUILDER, "toString", "()Ljava/lang/String;");


	public static final MethodReference METHOD_DEFAULT_CTOR = MethodReference
			.findOrCreate(TYPE_STRINGBUILDER, "<init>", "()V");

	public static final MethodReference METHOD_CTOR_STRING = MethodReference
			.findOrCreate(TYPE_STRINGBUILDER, "<init>", "(Ljava/lang/String;)V ");

	public static final TypeLabel INSTANCE = new StringListBuilderType();


	public static final Set<MethodReference> methods = Sets.newHashSet(METHOD_APPEND, METHOD_TOSTRING, METHOD_DEFAULT_CTOR, METHOD_CTOR_STRING);
	public static final Set<MethodReference> methodsAsReceiver = Sets.newHashSet(METHOD_APPEND);

	private static final Set<MethodReference> methodsReturnThis = Sets.newHashSet(METHOD_APPEND);
	private static final Set<MethodReference> methodsReturnString = Sets.newHashSet(METHOD_TOSTRING);

	@Override
	public boolean canBeUsedAsParamFor(MethodReference method, int index) {
		return false;
	}

	@Override
	public boolean canBeUsedAsReceiverFor(MethodReference method) {
		return methods.contains(method);
	}

	@Override
	public boolean canBeDefinedAsResultOf(MethodReference method) {
		return methodsAsReceiver.contains(method);
	}

	@Override
	public ReceiverInfo getReceiverUseInfo(MethodReference method) {
		if (methodsAsReceiver.contains(method)) {
			return new ReceiverInfo(true, Lists.<Integer>newLinkedList());
		} else {
			return new ReceiverInfo(false, Lists.<Integer>newLinkedList());
		}

	}

	@Override
	public boolean canReturnedValueBeLabeled(MethodReference method) {
		return methodsAsReceiver.contains(method);
	}

	@Override
	public Class<?> getOptimizedType() {
		return StringListBuilder.class;
	}

	@Override
	public String getCreationMethodName() {
		return "valueOf";
	}

	@Override
	public boolean compatibleWith(TypeLabel label) {
		return this == label || label instanceof SubstringStringType;
	}

	@Override
	public Class<?> getOriginalType() {
		return null;
	}

	@Override
	public String getToOriginalMethodName() {
		return null;
	}

	@Override
	protected Collection<MethodReference> methods() {
		return methods;
	}

	@Override
	public String toString() {
		return "StringListBuilder";
	}

	@Override
	public Class<?> getReturnType(MethodReference target) {
		if (methodsReturnThis.contains(target)) {
			return getOptimizedType();
		} else if (methodsReturnString.contains(target)) {
			return String.class;
		}

		return null;
	}

	@Override
	public List<Class<?>> getParams(MethodReference target) {
		if (target == METHOD_APPEND) {
			return Arrays.<Class<?>>asList(SubstringString.class);
		}
		return null;
	}

}
