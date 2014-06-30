package de.unifrankfurt.faststring.analysis.graph;

import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;
import com.ibm.wala.ssa.SSAInstruction.Visitor;

import de.unifrankfurt.faststring.analysis.IRMethod;
import de.unifrankfurt.faststring.analysis.model.DataFlowObject;

public abstract class DataFlowCreationVisitor<T extends DataFlowObject> extends Visitor {

	private T res;

	private int valueNumber;

	private IRMethod ir;

	public DataFlowCreationVisitor(IRMethod ir, int valueNumber) {
		this.ir = ir;
		this.valueNumber = valueNumber;
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
			
			Integer index = ir.getIndexFor(instruction);
			
			// null if it is not a bytecode instruction
			if (index != null) {
				result.setByteCodeIndex(ir.getByteCodeIndexFor(index));
				result.setLocalVariableIndices(ir.getLocalVariableIndices(index, valueNumber));
			} else {
				if (!(instruction instanceof SSAPhiInstruction)) {
					throw new IllegalStateException("no index found for: " + instruction);
				}
			}
			
			res = null;
			return result;
		}
	}
	
	protected int getValueNumber() {
		return valueNumber;
	}
}
