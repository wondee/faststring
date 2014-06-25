package de.unifrankfurt.faststring.analysis

import java.util.Set;

import com.ibm.wala.ssa.IR;

class IRHelper {

	static def Set<Integer> findLocalVariableIndex(IR ir, int index, int vn) {
		return (ir.localMap.findLocalsForValueNumber(index, vn) ?: []) as Set
	}
}
