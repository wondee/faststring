package de.unifrankfurt.faststring.analysis.graph;

import java.util.Map;

import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInstruction.Visitor;

import de.unifrankfurt.faststring.analysis.model.DataFlowObject;

public abstract class DataFlowCreationVisitor<T extends DataFlowObject> extends Visitor {

	private T res;
	private Map<SSAInstruction, Integer> instructionToIndexMap;
	
	public DataFlowCreationVisitor(Map<SSAInstruction, Integer> instructionToIndexMap) {
		this.instructionToIndexMap = instructionToIndexMap;
	}

	protected void setResult(T result) {
		if (this.res != null) {
			throw new IllegalStateException("result was attempted to be set more than once");
		}
		
		this.res = result;
	}
	
	public T create(SSAInstruction instruction) {
		instruction.visit(this);
		
		if (res == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			
			T result = res;
			
			Integer index = instructionToIndexMap.get(instruction);
			
			if (index != null) {
				result.setIndex(index);
			}
			
			res = null;
			return result;
		}
	}
	
	
}
