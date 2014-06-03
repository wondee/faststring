package de.unifrankfurt.faststring.analysis.graph;

import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInstruction.Visitor;

import de.unifrankfurt.faststring.analysis.model.DataFlowObject;

public abstract class DataFlowCreationVisitor<T extends DataFlowObject> extends Visitor {

	private T res;
	
	protected void setResult(T result) {
		if (this.res != null) {
			throw new IllegalStateException("result was attempted to be set more than once");
		}
		
		this.res = result;
	}
	
	public T retrieveResult() {
		T result = res;
		res = null;
		return result;
	}
	
	public T create(SSAInstruction instruction) {
		instruction.visit(this);
		
		if (res == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		}
		
		return retrieveResult();
	}
	
	
}
