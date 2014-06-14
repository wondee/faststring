package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.util.collections.Pair;

import de.unifrankfurt.faststring.analysis.util.IRUtil;

public class IRMethod {

	private IR ir;
	private DefUse defUse;

	private Map<SSAInstruction, Integer> instructionToIndexMap;
	private Map<Pair<String, Integer>, Collection<Integer>> localNamesMap;
	
	
	public IRMethod(IR ir, DefUse defUse) {
		this.ir = ir;
		this.defUse = defUse;
	}
	
	public Integer getIndexFor(SSAInstruction instruction) {
		if (instructionToIndexMap == null) {
			instructionToIndexMap = IRUtil.createInstructionToIndexMap(ir);
		}
		
		return instructionToIndexMap.get(instruction);
	}

	public String getMethodSignature() {
		return ir.getMethod().getSignature();
	}

	public SSAInstruction getDef(int v) {
		return defUse.getDef(v);
	}
	
	public Iterator<SSAInstruction> getUses(int v) {
		return defUse.getUses(v);
	}	
	
	public Collection<Integer> getLocalVariableIndex(int bcIndex, int valueNumber) {
		
		String[] localNames = ir.getLocalNames(bcIndex, valueNumber);
		
		if (localNamesMap == null) {
			localNamesMap = IRUtil.createLocalNamesMap(ir.getMethod());
		}
		
		Set<Integer> set = Sets.newHashSet();
		
		if (localNames != null) {
		
			for (String name : localNames) {
				Pair<String, Integer> pair = Pair.make(name, bcIndex);
				Collection<Integer> c = localNamesMap.get(pair);
				if (c != null) {
					set.addAll(c);
				} else {
					System.out.printf("nothing found for %s\n", pair.toString());
				}
			}
		} else {
			System.out.printf("nothing found for %d %d\n", bcIndex, valueNumber);
		}
			
		return set;
	}

	public String[] getLocalNames(int index, int vn) {
		return ir.getLocalNames(index, vn);
	}
	
	public Set<Integer> getParams() {
		return Sets.newHashSet(Ints.asList(ir.getParameterValueNumbers()));
	}

	public SSAInstruction[] getInstructions() {
		return ir.getInstructions();
	}

	public boolean isConstant(int v) {
		return ir.getSymbolTable().isConstant(v);
	}

	
}
