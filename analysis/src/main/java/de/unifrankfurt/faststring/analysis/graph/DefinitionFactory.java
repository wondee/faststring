package de.unifrankfurt.faststring.analysis.graph;

import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;

import de.unifrankfurt.faststring.analysis.util.IRUtil;


public class DefinitionFactory extends  DataFlowCreationStrategy<Definition> {
	
	
	@Override
	protected Definition createInvoke(SSAInvokeInstruction invoke) {
		return Definition.createCallResultDefinition(invoke.getDeclaredTarget(), invoke.getReceiver());
	}

	@Override
	protected Definition createPhi(SSAPhiInstruction instruction) {
		return Definition.createPhiDefinitionInfo(IRUtil.getUsesList(instruction));
	}
	
}
