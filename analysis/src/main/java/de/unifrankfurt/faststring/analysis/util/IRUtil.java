package de.unifrankfurt.faststring.analysis.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.ibm.wala.shrikeBT.Util;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

public final class IRUtil {

	public static final TypeReference STRING_TYPE = TypeReference.findOrCreate(
			ClassLoaderReference.Application, "Ljava/lang/String");
	
	
	public static void main(String[] args) {
		System.out.println(Util.makeType(String.class));
		
	}
	
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

	/**
	 * creates a {@link List} out of all uses found in the given {@link SSAInstruction} starting with the use of index 
	 * {@code startsWith}.
	 * 
	 * @param ins the {@code SSAInstruction} to look for uses
	 * @param startWith the index of the use to start with adding to the list
	 * @return a list containing the found uses
	 */
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
		return findUseIndex(v, invoke, 1);
	}
	
	public static int findUseIndex(int v, SSAInvokeInstruction invoke, int startWith) {
		// start with 1 because 0 is the receiver
		for (int i = startWith; i < invoke.getNumberOfUses(); i++) {
			if (invoke.getUse(i) == v) {
				return i;
			}
		}
		throw new IllegalStateException("value number not found in uses list");
	}
	
	public static Set<Integer> getParamsFromIR(IR ir) {
		return Sets.newHashSet(Ints.asList(ir.getParameterValueNumbers()));
	}

	public static Map<SSAInstruction, Integer> createInstructionToIndexMap(IR ir) {
		Map<SSAInstruction, Integer> map = Maps.newHashMap();
		
		for (int i = 0; i < ir.getInstructions().length; i++) {
			SSAInstruction instruction = ir.getInstructions()[i];
			
			if (instruction != null) {
				Integer old = map.put(instruction, i);
				if (old != null) {
					throw new IllegalStateException("instruction was set before: actual index = " + i + 
							"; old index = " + old + "; instruction = " + instruction);
				}
			}
		}
		
		return map;
	}
	
}
