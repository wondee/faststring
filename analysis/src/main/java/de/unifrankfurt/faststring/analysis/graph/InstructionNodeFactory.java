package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.ibm.wala.shrikeBT.IInstruction;
import com.ibm.wala.ssa.SSAArrayStoreInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInstruction.Visitor;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;
import com.ibm.wala.ssa.SSAPutInstruction;
import com.ibm.wala.ssa.SSAReturnInstruction;

import de.unifrankfurt.faststring.analysis.IRMethod;
import de.unifrankfurt.faststring.analysis.util.IRUtil;

public class InstructionNodeFactory extends Visitor  {

	private InstructionNode res;
	private IRMethod ir;
	
	private Map<SSAInstruction, InstructionNode> cache = Maps.newHashMap();
	
	public InstructionNodeFactory(IRMethod ir) {
		this.ir = ir;
	}

	public InstructionNode create(SSAInstruction instruction) {
		if (!cache.containsKey(instruction)) {
		
			instruction.visit(this);
			
			if (res == null) {
				throw new UnsupportedOperationException("not implemented for " + instruction);
			} else {
				
				InstructionNode result = res;
				res = null;
				Integer index = ir.getIndexFor(instruction);
				
				// null if it is not a bytecode instruction
				if (index != null) {
					result.setByteCodeIndex(ir.getByteCodeIndexFor(index));
					
					List<Integer> vs = IRUtil.getUses(instruction);
					
					int def = instruction.getDef();
					if (def != -1) {
						vs.add(def);
					}
					
					for (int v : vs) {
						result.addLocalVariableIndices(v, ir.getLocalVariableIndices(index, v));
					}
					
					if (result instanceof MethodCallInstruction) {
						result.addLocalVariableIndices(def, findPossibleDefLocal(instruction));
					}
					
				} else {
					if (!(instruction instanceof SSAPhiInstruction)) {
						throw new IllegalStateException("no index found for: " + instruction);
					}
				}
				
				cache.put(instruction, result);
				
				return result;
			}
		} else {
			return cache.get(instruction);
		}
		
	}

	private Set<Integer> findPossibleDefLocal(SSAInstruction instruction) {
		IInstruction[] instructions = ir.getRemainingInstructionsOfBlock(instruction);
		
		LocalVariableSolver solver = new LocalVariableSolver(instruction.getDef());
		
		for (int i = 0; i < instructions.length && !solver.isFinished(); i++) {
			IInstruction ins = instructions[i];
			ins.visit(solver);
			
		}
		
		return solver.getPossibleLocals();
	}


	@Override
	public void visitInvoke(SSAInvokeInstruction invoke) {
		res = new MethodCallInstruction(invoke);
	}
	
	@Override
	public void visitPhi(SSAPhiInstruction instruction) {
		res = new PhiInstructionNode(instruction.getDef(), IRUtil.getUses(instruction));
	
	}

	@Override
	public void visitReturn(SSAReturnInstruction instruction) {
		res = new ReturnInstruction(instruction.getResult());
	}

	@Override
	public void visitPut(SSAPutInstruction instruction) {
		res = new ReturnInstruction(instruction.getUse(0));
		
	}

	@Override
	public void visitArrayStore(SSAArrayStoreInstruction instruction) {
		res = new ReturnInstruction(instruction.getValue());
	}

}
