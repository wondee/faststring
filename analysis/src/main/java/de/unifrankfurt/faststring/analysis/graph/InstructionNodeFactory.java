package de.unifrankfurt.faststring.analysis.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.ibm.wala.ssa.SSAArrayStoreInstruction;
import com.ibm.wala.ssa.SSAGetInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInstruction.Visitor;
import com.ibm.wala.ssa.SSAArrayLoadInstruction;
import com.ibm.wala.ssa.SSACheckCastInstruction;
import com.ibm.wala.ssa.SSAConditionalBranchInstruction;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSANewInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;
import com.ibm.wala.ssa.SSAPutInstruction;
import com.ibm.wala.ssa.SSAReturnInstruction;

import de.unifrankfurt.faststring.analysis.AnalyzedMethod;
import de.unifrankfurt.faststring.analysis.AnalyzedMethod.LocalInfo;
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

		ConstantNode constant = new ConstantNode(method.getConstantIndex(v), v);
		Collection<Integer> locals = method.getLocalForDef(0, v);
		constant.addLocalVariableIndices(v, locals);

		return constant;
	}

	public InstructionNode create(SSAInstruction instruction) {
		LOG.trace("creating instruction node");

		if (!cache.containsKey(instruction)) {

			instruction.visit(this);

			if (res == null) {
				throw new UnsupportedOperationException("not implemented for " + instruction);
			} else {

				InstructionNode result = res;
				res = null;
				Integer index = method.getIndexFor(instruction);

				result.setByteCodeIndex(index);

				LOG.trace("check locals");
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
		if (instruction.hasDef()) {
			int def = instruction.getDef();

			result.addLocalVariableIndices(def, method.getLocalForDef(index, def));

			LOG.debug("determine store instruction for v={} at {}", def, instruction);

			LocalInfo store = method.getStoreFor(0, 1, (instruction instanceof SSAPhiInstruction) ? index - 1 : index);
			if (store != null) {
				result.addLocalVariableIndices(def, Arrays.asList(store.local()));
				result.setStore(store.local(), store.bcIndex());
			}

		}
	}


	private void checkLocalsForUses(SSAInstruction instruction, InstructionNode result, Integer index) {
		List<Integer> vs = IRUtil.getUses(instruction);

		for (int i = 0; i < vs.size(); i++) {
			int v = vs.get(i);

			result.addLocalVariableIndices(v, method.getLocalForUse(index, v));
			LOG.debug("determine load instruction for v={} at {}", v, instruction);
			LocalInfo load = method.getLoadFor(i, vs.size(), index);
			if (load != null) {
				result.addLocalVariableIndices(v, Arrays.asList(load.local()));

				result.addLoad(load.local(), load.bcIndex());
			}
		}
	}

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

	@Override
	public void visitNew(SSANewInstruction instruction) {
		res = new NewNode(instruction.getDef(), instruction.getConcreteType());
	}

	@Override
	public void visitGet(SSAGetInstruction instruction) {
		res = new GetNode();
	}

	@Override
	public void visitConditionalBranch(SSAConditionalBranchInstruction instruction) {
		// TODO implement support
		res = new ConditionalBranchNode();
	}

	@Override
	public void visitCheckCast(SSACheckCastInstruction instruction) {
		// TODO implement support
		res = new CheckCastNode();
	}

	@Override
	public void visitArrayLoad(SSAArrayLoadInstruction instruction) {
		res = new GetNode();
	}

}
