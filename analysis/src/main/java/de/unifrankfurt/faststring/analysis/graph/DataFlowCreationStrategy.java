package de.unifrankfurt.faststring.analysis.graph;

import com.ibm.wala.ssa.SSAArrayLengthInstruction;
import com.ibm.wala.ssa.SSAArrayLoadInstruction;
import com.ibm.wala.ssa.SSAArrayStoreInstruction;
import com.ibm.wala.ssa.SSABinaryOpInstruction;
import com.ibm.wala.ssa.SSACheckCastInstruction;
import com.ibm.wala.ssa.SSAComparisonInstruction;
import com.ibm.wala.ssa.SSAConditionalBranchInstruction;
import com.ibm.wala.ssa.SSAConversionInstruction;
import com.ibm.wala.ssa.SSAGetCaughtExceptionInstruction;
import com.ibm.wala.ssa.SSAGetInstruction;
import com.ibm.wala.ssa.SSAGotoInstruction;
import com.ibm.wala.ssa.SSAInstanceofInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInstruction.Visitor;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSALoadMetadataInstruction;
import com.ibm.wala.ssa.SSAMonitorInstruction;
import com.ibm.wala.ssa.SSANewInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;
import com.ibm.wala.ssa.SSAPiInstruction;
import com.ibm.wala.ssa.SSAPutInstruction;
import com.ibm.wala.ssa.SSAReturnInstruction;
import com.ibm.wala.ssa.SSASwitchInstruction;
import com.ibm.wala.ssa.SSAThrowInstruction;
import com.ibm.wala.ssa.SSAUnaryOpInstruction;

public abstract class DataFlowCreationStrategy<T extends DataFlowCreationObject> extends Visitor {

	private T res;
	
	private void setResult(T result) {
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
		return res;
	}
	
	@Override
	public void visitGoto(SSAGotoInstruction instruction) {
		T result = createGoto(instruction);
		
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}
	
	protected T createGoto(SSAGotoInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitArrayLoad(SSAArrayLoadInstruction instruction) {
		T result = createArrayLoad(instruction);
		
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createArrayLoad(SSAArrayLoadInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitArrayStore(SSAArrayStoreInstruction instruction) {
		T result = createArrayStore(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createArrayStore(SSAArrayStoreInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitBinaryOp(SSABinaryOpInstruction instruction) {
		T result = createBinaryOp(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createBinaryOp(SSABinaryOpInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitUnaryOp(SSAUnaryOpInstruction instruction) {
		T result = createUnaryOp(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createUnaryOp(SSAUnaryOpInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitConversion(SSAConversionInstruction instruction) {
		T result = createConversion(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createConversion(SSAConversionInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitComparison(SSAComparisonInstruction instruction) {
		T result = createComparison(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createComparison(SSAComparisonInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitConditionalBranch(SSAConditionalBranchInstruction instruction) {
		T result = createConditionalBranch(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createConditionalBranch(SSAConditionalBranchInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitSwitch(SSASwitchInstruction instruction) {
		T result = createSwitch(instruction);
		
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createSwitch(SSASwitchInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitReturn(SSAReturnInstruction instruction) {
		T result = createReturn(instruction);
		if (result  == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createReturn(SSAReturnInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitGet(SSAGetInstruction instruction) {
		T result = createGet(instruction);
		if (result  == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createGet(SSAGetInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitPut(SSAPutInstruction instruction) {
		T result = createPut(instruction);
		if (result  == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createPut(SSAPutInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitInvoke(SSAInvokeInstruction instruction) {
		T result = createInvoke(instruction);
		if (result  == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createInvoke(SSAInvokeInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitNew(SSANewInstruction instruction) {
		T result = createNew(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createNew(SSANewInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitArrayLength(SSAArrayLengthInstruction instruction) {
		T result = createArrayLength(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createArrayLength(SSAArrayLengthInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitThrow(SSAThrowInstruction instruction) {
		T result = createThrow(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}
	
	protected T createThrow(SSAThrowInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitMonitor(SSAMonitorInstruction instruction) {
		T result = createMonitor(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createMonitor(SSAMonitorInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitCheckCast(SSACheckCastInstruction instruction) {
		T result = createCheckCast(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}
	
	protected T createCheckCast(SSACheckCastInstruction instruction) {
		return null;
	}
	
	
	@Override
	public void visitInstanceof(SSAInstanceofInstruction instruction) {
		T result = createInstanceof(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createInstanceof(SSAInstanceofInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitPhi(SSAPhiInstruction instruction) {
		T result = createPhi(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createPhi(SSAPhiInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitPi(SSAPiInstruction instruction) {
		T result = createPi(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createPi(SSAPiInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitGetCaughtException(SSAGetCaughtExceptionInstruction instruction) {
		T result = createGetCaughtException(instruction);
		if (result == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createGetCaughtException(SSAGetCaughtExceptionInstruction instruction) {
		return null;
	}
	
	@Override
	public void visitLoadMetadata(SSALoadMetadataInstruction instruction) {
		T result = createLoadMetadata(instruction);
		if (result  == null) {
			throw new UnsupportedOperationException("not implemented for " + instruction);
		} else {
			setResult(result);
		}
		
	}

	protected T createLoadMetadata(SSALoadMetadataInstruction instruction) {
		return null;
	}
	
}
