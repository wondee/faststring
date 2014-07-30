package de.unifrankfurt.faststring.analysis.label;

import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import de.unifrankfurt.faststring.core.SubstringString;

public class MockLabel extends BaseTypeLabel {

	public static final TypeLabel INSTANCE = new MockLabel();
	
	public static final TypeReference TYPE_STRING = TypeReference.findOrCreate(
			ClassLoaderReference.Application, "Ljava/lang/String");

	public static final MethodReference METHOD_SUBSTRING = MethodReference
			.findOrCreate(TYPE_STRING, "substring", "(II)Ljava/lang/String;");
	

	public static final MethodReference METHOD_SUBSTRING_DEFAULT_START = MethodReference
			.findOrCreate(TYPE_STRING, "substring", "(I)Ljava/lang/String;");
	
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
	protected Collection<MethodReference> methods() {
		return ImmutableList.of(METHOD_SUBSTRING, METHOD_SUBSTRING_DEFAULT_START);
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

	@Override
	public String getReturnType(MethodReference target) {
		return null;
	}

	@Override
	public String getParams(MethodReference target) {
		// TODO Auto-generated method stub
		return null;
	}

}
