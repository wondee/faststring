package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.ShrikeCTMethod;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;

import de.unifrankfurt.faststring.analysis.util.IRUtil;

public class IRMethod {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(IRMethod.class);

	private IR ir;
	private DefUse defUse;

	private Map<SSAInstruction, Integer> instructionToIndexMap;


	public IRMethod(IR ir, DefUse defUse) {
		this.ir = ir;
		this.defUse = defUse;
	}

	public Integer getIndexFor(SSAInstruction instruction) {
		if (instructionToIndexMap == null) {
			instructionToIndexMap = IRUtil.createInstructionToIndexMap(ir);
		}

		return instructionToIndexMap.get(instruction);
	}

	public String getMethodSignature() {
		return ir.getMethod().getSignature();
	}

	public SSAInstruction getDef(int v) {
		return defUse.getDef(v);
	}

	public Iterator<SSAInstruction> getUses(int v) {
		return defUse.getUses(v);
	}

	public Collection<Integer> getLocalVariableIndices(int bcIndex, int valueNumber) {

		Set<Integer> pointers = IRUtil.findAllDefPointersFor(defUse, valueNumber);

		Set<Integer> locals = Sets.newHashSet();

		for (Integer p : pointers) {
			locals.addAll(IRHelper.findLocalVariableIndex(ir, bcIndex, p));
		}

		return locals;

	}

	public Set<Integer> getParams() {
		return Sets.newHashSet(Ints.asList(ir.getParameterValueNumbers()));
	}

	public SSAInstruction[] getInstructions() {
		return ir.getInstructions();
	}

	public boolean isConstant(int v) {
		return ir.getSymbolTable().isConstant(v);
	}

	public Object getConstantValue(int v) {
		return ir.getSymbolTable().getConstantValue(v);
	}

	public int getParamIndexFor(int v) {
		int[] params = ir.getSymbolTable().getParameterValueNumbers();

		for (int i = 0; i < params.length; i++) {
			int p = params[i];

			if (p == v) {
				return i;
			}
		}

		return -1;
	}

	public int getByteCodeIndexFor(Integer index) {
		try {
			IBytecodeMethod method = (IBytecodeMethod) ir.getMethod();
			return method.getBytecodeIndex(index);
		} catch (InvalidClassFileException e) {
			throw new IllegalStateException("could not read class file", e);
		}
	}

	public int getMaxLocals() {
		return ((ShrikeCTMethod)ir.getMethod()).getMaxLocals();
	}

	public Set<Integer> findAllUsesPhiPointer(Integer ref) {
		return IRUtil.findAllUsesPointersFor(defUse, ref);

	}

}
