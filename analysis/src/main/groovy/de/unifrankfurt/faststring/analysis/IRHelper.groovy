package de.unifrankfurt.faststring.analysis

import java.util.Set;

import com.ibm.wala.ssa.IR;

/**
 * Helper class for accessing the private methode {@code findLocalsForValueNumber(int, int)}
 *
 * @author markus
 *
 */
class IRHelper {

	/**
	 *
	 * @param ir the {@link IR}
	 * @param index the index of the bytecode instruction
	 * @param vn the value number
	 * @return a {@link Set} of local variables that the given value number represents at
	 * this bytecode index
	 */
	static def Set<Integer> findLocalVariableIndex(IR ir, int index, int vn) {
		return (ir.localMap.findLocalsForValueNumber(index, vn) ?: []) as Set
	}
}
