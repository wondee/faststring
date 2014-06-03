package de.unifrankfurt.faststring.analysis.util;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInvokeInstruction;
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

	public static final MethodReference METHOD_STRING_VALUE_OF = MethodReference
			.findOrCreate(STRING_TYPE, "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;");
	
	
	public static final List<Integer> EMPTY_LIST = ImmutableList.of();
	
	
	private IRUtil() {
		// empty
	}

	public static List<Integer> getUsesList(SSAInstruction ins, int startWith) {
		int size = ins.getNumberOfUses();
		List<Integer> list = Lists.newArrayListWithExpectedSize(size);
		
		for (int i = startWith; i < size; i++) {
			list.add(ins.getUse(i));
		}
		
		return list;
		
	}

	public static List<Integer> getUsesList(SSAInstruction ins) {
		return getUsesList(ins, 0);
	}

	public static int findUseIndex(int v, SSAInvokeInstruction invoke) {
		// start with 1 because 0 is the receiver
		for (int i = 1; i < invoke.getNumberOfUses(); i++) {
			if (invoke.getUse(i) == v) {
				return i;
			}
		}
		throw new IllegalStateException("value number not found in uses list");
	}
	
}
