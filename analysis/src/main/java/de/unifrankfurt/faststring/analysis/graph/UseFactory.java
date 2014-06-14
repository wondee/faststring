package de.unifrankfurt.faststring.analysis.graph;

import com.ibm.wala.ssa.SSAArrayStoreInstruction;
import com.ibm.wala.ssa.SSAInstruction.IVisitor;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;
import com.ibm.wala.ssa.SSAPutInstruction;
import com.ibm.wala.ssa.SSAReturnInstruction;

import de.unifrankfurt.faststring.analysis.IRMethod;
import de.unifrankfurt.faststring.analysis.model.Use;
import de.unifrankfurt.faststring.analysis.util.IRUtil;

public class UseFactory extends DataFlowCreationVisitor<Use> implements IVisitor  {

	public UseFactory(IRMethod ir, int valueNumber) {
		super(ir, valueNumber);
	}

	@Override
	public void visitInvoke(SSAInvokeInstruction invoke) {
		Use use;
		
		// determine if the use refers to the receiver
		int def = invoke.getDef();
		
		if (invoke.getReceiver() != getValueNumber() || invoke.isStatic()) {
			// if def is -1 than invoke special (c'tor) is called, so use the receiver instead
			if (def == -1) {
				def = invoke.getReceiver();
			}
			
			int startWith = (invoke.isStatic()) ? 0 : 1;
			
			use = Use.createPassedAsParam(invoke.getDeclaredTarget(), def, IRUtil.findUseIndex(getValueNumber(), invoke, startWith));
		} else {
			use = Use.createUsedAsReceiver(invoke.getDeclaredTarget(), def, IRUtil.getUsesList(invoke, 1));
		}
		
		setResult(use);
	}
	
	@Override
	public void visitReturn(SSAReturnInstruction instruction) {
		setResult(Use.createReturned());
	}
	
	@Override
	public void visitPhi(SSAPhiInstruction instruction) {
		setResult(Use.createUsedInPhi(instruction.getDef()));
	}
	
	@Override
	public void visitPut(SSAPutInstruction instruction) {
		setResult(Use.createReturned());
	}
	
	@Override
	public void visitArrayStore(SSAArrayStoreInstruction instruction) {
		setResult(Use.createReturned());
	}


}
