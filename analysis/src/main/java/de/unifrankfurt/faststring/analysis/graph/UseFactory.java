package de.unifrankfurt.faststring.analysis.graph;

import com.ibm.wala.ssa.SSAArrayStoreInstruction;
import com.ibm.wala.ssa.SSAInstruction.IVisitor;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;
import com.ibm.wala.ssa.SSAPutInstruction;
import com.ibm.wala.ssa.SSAReturnInstruction;

import de.unifrankfurt.faststring.analysis.util.IRUtil;

public class UseFactory extends DataFlowCreationStrategy<Use> implements IVisitor  {


	private int v;

	public UseFactory(int valueNumber) {
		v = valueNumber;
	}

	@Override
	protected Use createInvoke(SSAInvokeInstruction invoke) {
		Use use;
		
		// determine if the use refers to the receiver
		int def = invoke.getDef();
		if (invoke.getReceiver() != v) {
			// if def is null than invoke special (c'tor) is called so use the receiver instead
			if (def == -1) {
				def = invoke.getReceiver();
			}
			
			use = Use.createPassedAsParam(invoke.getDeclaredTarget(), def, IRUtil.findUseIndex(v, invoke));
		} else {
			use = Use.createUsedAsReceiver(invoke.getDeclaredTarget(), def, IRUtil.getUsesList(invoke, 1));
		}
		return use;
	}
	
	@Override
	protected Use createReturn(SSAReturnInstruction instruction) {
		return Use.createReturned();
	}
	
	@Override
	protected Use createPhi(SSAPhiInstruction instruction) {
		return Use.createUsedInPhi(instruction.getDef());
	}
	
	public Use create(SSAPhiInstruction phi) {
		return Use.createUsedInPhi(phi.getDef());
	}
	
	@Override
	protected Use createPut(SSAPutInstruction instruction) {
		// TODO to change when going interprozedural
		return Use.createReturned();
	}
	
	@Override
	protected Use createArrayStore(SSAArrayStoreInstruction instruction) {
		// TODO maybe create a StoredInArrays to try to replace the whole arrays instance
		return Use.createReturned();
	}


}
