package de.unifrankfurt.faststring.analysis;

import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

public final class IRUtil {

	public static final TypeReference STRING_TYPE = TypeReference.findOrCreate(
			ClassLoaderReference.Application, "Ljava/lang/String");
	
	
	public static final MethodReference METHOD_SUBSTRING = MethodReference
			.findOrCreate(STRING_TYPE, "substring", "(II)Ljava/lang/String;");

	public static final MethodReference METHOD_SUBSTRING_DEFAULT_START = MethodReference
			.findOrCreate(STRING_TYPE, "substring", "(I)Ljava/lang/String;");

	private IRUtil() {
		// empty
	}

}
