package de.unifrankfurt.faststring.analysis.graph;

import java.util.Map;

import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;

import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.util.IRUtil;


public class DefinitionFactory extends  DataFlowCreationVisitor<Definition> {
	
	
	public DefinitionFactory(Map<SSAInstruction, Integer> instructionToIndexMap) {
		super(instructionToIndexMap);
	}

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
