package de.unifrankfurt.faststring.analysis.graph;

import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;

import de.unifrankfurt.faststring.analysis.util.IRUtil;


public class DefinitionFactory extends  DataFlowCreationVisitor<Definition> {
	
	
	@Override
	public void visitInvoke(SSAInvokeInstruction invoke) {
		Definition result = Definition.createCallResultDefinition(invoke.getDeclaredTarget(), invoke.getReceiver());
		setResult(result);
	}
	
	@Override
	public void visitPhi(SSAPhiInstruction instruction) {
		Definition result = Definition.createPhiDefinitionInfo(IRUtil.getUsesList(instruction));
		setResult(result);
	
	}
	
}
