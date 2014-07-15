package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ibm.wala.ssa.SSAArrayStoreInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInstruction.Visitor;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;
import com.ibm.wala.ssa.SSAPutInstruction;
import com.ibm.wala.ssa.SSAReturnInstruction;

import de.unifrankfurt.faststring.analysis.AnalyzedMethod;
import de.unifrankfurt.faststring.analysis.util.IRUtil;

public class InstructionNodeFactory extends Visitor  {

	private InstructionNode res;
	private AnalyzedMethod method;

	private Map<SSAInstruction, InstructionNode> cache = Maps.newHashMap();

	public InstructionNodeFactory(AnalyzedMethod ir) {
		this.method = ir;
	}


	public InstructionNode createConstant(int v) {

		ConstantNode constant = new ConstantNode(method.getConstantValue(v), method.getConstantIndex(v));
		Collection<Integer> locals = method.getLocalVariableIndices(0, v);
		constant.addLocalVariableIndices(v, locals);

		return constant;
	}

	public InstructionNode create(SSAInstruction instruction) {
		if (!cache.containsKey(instruction)) {

			instruction.visit(this);

			if (res == null) {
				throw new UnsupportedOperationException("not implemented for " + instruction);
			} else {

				InstructionNode result = res;
				res = null;
				Integer index = method.getIndexFor(instruction);

				result.setByteCodeIndex(index);

				List<Integer> vs = IRUtil.getUses(instruction);

				if (vs.size() == Sets.newHashSet(vs).size()) {
					int def = instruction.getDef();
					if (def != -1) {
						vs.add(def);
					}

					for (int v : vs) {
						result.addLocalVariableIndices(v, method.getLocalVariableIndices(index, v));
						for (int local : result.getLocals(v)) {
							method.getLoadFor(local, instruction);
						}
					}

				} else {
					throw new UnsupportedOperationException("now we need to implement it");
				}



//				if (result instanceof MethodCallNode) {
//					method.getStoreForLocal(index, result.getDef());
//				}

				cache.put(instruction, result);

				return result;
			}
		} else {
			return cache.get(instruction);
		}

	}
//
//	private Set<Integer> findPossibleDefLocal(SSAInstruction instruction) {
//		IInstruction[] instructions = ir.getLastIndexOfSinglePathReachedBlock(instruction);
//
//		LocalVariableSolver solver = new LocalVariableSolver(instruction.getDef());
//
//		for (int i = 0; i < instructions.length && !solver.isFinished(); i++) {
//			IInstruction ins = instructions[i];
//			ins.visit(solver);
//
//		}
//
//		return solver.getPossibleLocals();
//	}


	@Override
	public void visitInvoke(SSAInvokeInstruction invoke) {
		res = new MethodCallNode(invoke);
	}

	@Override
	public void visitPhi(SSAPhiInstruction instruction) {
		res = new PhiNode(instruction.getDef(), IRUtil.getUses(instruction));

	}

	@Override
	public void visitReturn(SSAReturnInstruction instruction) {
		res = new ReturnNode(instruction.getResult());
	}

	@Override
	public void visitPut(SSAPutInstruction instruction) {
		res = new ReturnNode(instruction.getUse(0));

	}

	@Override
	public void visitArrayStore(SSAArrayStoreInstruction instruction) {
		res = new ReturnNode(instruction.getValue());
	}

}
