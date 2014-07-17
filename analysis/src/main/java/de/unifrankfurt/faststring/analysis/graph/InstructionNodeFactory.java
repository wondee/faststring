package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static Logger LOG = LoggerFactory.getLogger(InstructionNodeFactory.class);

	private InstructionNode res;
	private AnalyzedMethod method;

	private Map<SSAInstruction, InstructionNode> cache = Maps.newHashMap();

	public InstructionNodeFactory(AnalyzedMethod ir) {
		this.method = ir;
	}


	public InstructionNode createConstant(int v) {

		ConstantNode constant = new ConstantNode(method.getConstantValue(v), method.getConstantIndex(v));
		Collection<Integer> locals = method.getLocalForDef(0, v);
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

				checkLocalsForDef(instruction, result, index);
				checkLocalsForUses(instruction, result, index);

				cache.put(instruction, result);

				return result;
			}
		} else {
			return cache.get(instruction);
		}

	}


	private void checkLocalsForDef(SSAInstruction instruction, InstructionNode result, Integer index) {
		int def = instruction.getDef();
		if (def != -1) {
			result.addLocalVariableIndices(def, method.getLocalForDef(index, def));
			for (int local : result.getLocals(def)) {
				LOG.debug("determine store instruction for v={} at {}", def, instruction);
				int storeIndex = method.getStoreFor(local, 0, 1, 
						(instruction instanceof SSAPhiInstruction) ? index - 1 : index);

				result.addStore(local, storeIndex);
			}
			
		}
	}


	private void checkLocalsForUses(SSAInstruction instruction, InstructionNode result, Integer index) {
		List<Integer> vs = IRUtil.getUses(instruction);

		if (vs.size() == Sets.newHashSet(vs).size()) {
			for (int v : vs) {
				result.addLocalVariableIndices(v, method.getLocalForUse(index, v));
				for (int local : result.getLocals(v)) {
					LOG.debug("determine load instruction for v={} at {}", v, instruction);
					int loadIndex = method.getLoadFor(local, vs.indexOf(v), vs.size(), index);
	
					result.addLoad(local, loadIndex);
				}
			}
		} else {
			throw new UnsupportedOperationException("now we need to implement it");
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
