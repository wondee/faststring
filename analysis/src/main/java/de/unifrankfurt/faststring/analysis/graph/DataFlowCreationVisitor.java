package de.unifrankfurt.faststring.analysis.graph;

import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInstruction.Visitor;

public abstract class DataFlowCreationVisitor<T extends DataFlowCreationObject> extends Visitor {

	private T res;
	
	protected void setResult(T result) {
		if (this.res != null) {
			throw new IllegalStateException("result was attempted to be set more than once");
		}
		
		this.res = result;
	}
	
	public T getResult() {
		return res;
	}
	
	public void reset() {
		res = null;
	}
	
	public T create(SSAInstruction instruction) {
		instruction.visit(this);
		
		if (res == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		}
		
		return res;
	}
	
	
}
