package de.unifrankfurt.faststring.analysis.wala;

import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.TypeReference;

public final class IRUtil {

	public static final TypeReference STRING_TYPE = TypeReference.findOrCreate(ClassLoaderReference.Application, "Ljava/lang/String");
	
	
	private IRUtil() {
		// empty
	}
	
}
