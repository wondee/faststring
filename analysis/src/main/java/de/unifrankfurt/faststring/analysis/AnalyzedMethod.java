package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.ShrikeCTMethod;
import com.ibm.wala.classLoader.ShrikeClass;
import com.ibm.wala.shrikeBT.IInstruction;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSACFG.BasicBlock;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;
import com.ibm.wala.util.collections.Pair;

import de.unifrankfurt.faststring.analysis.util.IRUtil;

/**
 * Represents a method from bytecode which is about to be analyzed
 * <p>
 * This class provides access to the methods bytecode instructions and offers functionality to
 * determine locals with their <code>load</code> and <code>store</code> instructions for value numbers.
 *
 */
public class AnalyzedMethod {

	private static final Logger LOG = LoggerFactory.getLogger(AnalyzedMethod.class);

	private IR ir;
	private DefUse defUse;

	/** mapping instruction -> bytecode index */
	private Map<SSAInstruction, Integer> instructionToIndexMap;

	/** map value number (phi instruction) -> block index */
	private Map<Integer, Integer> phi2BlockMap;

	/** all instructions of this method */
	private IInstruction[] bytecodeInstructions;

	public AnalyzedMethod(IR ir, DefUse defUse) {
		Preconditions.checkNotNull(ir, "ir is null");
		Preconditions.checkNotNull(defUse, "defUse is null");

		this.ir = ir;
		this.defUse = defUse;
	}

	/**
	 * @param instruction
	 * @return the bytecode index in the WALA parsed method body
	 */
	public Integer getIndexFor(SSAInstruction instruction) {

		LOG.trace("get bytecode index");

		if (instruction instanceof SSAPhiInstruction) {
			return getIndexForPhi((SSAPhiInstruction) instruction);
		} else {
			if (instructionToIndexMap == null) {
				LOG.trace("creating instruction index map");
				instructionToIndexMap = IRUtil.createInstructionToIndexMap(ir);
			}
			return instructionToIndexMap.get(instruction);
		}

	}

	/**
	 * @return the name of the method analyzed
	 */
	public String getMethodName() {
		return ir.getMethod().getName().toString();
	}

	/**
	 * @return the signiture of the method analyzed
	 */
	public String getSignature() {
		return ir.getMethod().getSignature();
	}


	/**
	 * @param v
	 * @return the {@link SSAInstruction} which introduces the given value number
	 */
	public SSAInstruction getDef(int v) {
		return defUse.getDef(v);
	}

	/**
	 * @param v
	 * @return a list of all {@link SSAInstruction}s that use the given value number
	 */
	public List<SSAInstruction> getUses(int v) {
		return Lists.newArrayList(defUse.getUses(v));
	}

	/**
	 * Tries to determine the local for a value number when it is defined
	 *
	 * @param bcIndex the bytecode index where the value number is defined
	 * @param valueNumber the value number too check
	 * @return all possible locals for that value number
	 */
	public Collection<Integer> getLocalForDef(int bcIndex, int valueNumber) {

		int lastIndex = findLastIndexBeforeBranch(bcIndex);

		Set<Integer> locals = Sets.newHashSet();

		for (int i = bcIndex; i < lastIndex; i++) {
			locals.addAll(IRHelper.findLocalVariableIndex(ir, i, valueNumber));
		}

		return locals;
	}

	/**
	 * Tries to determine the local for a value number when it is used
	 *
	 * @param bcIndex the bytecode index where the value number is used
	 * @param valueNumber the value number too check
	 * @return all possible locals for that value number
	 */
	public Collection<Integer> getLocalForUse(int bcIndex, int valueNumber) {

		int firstIndex = findFirstIndexBeforeBranch(bcIndex);

		Set<Integer> locals = Sets.newHashSet();

		for (int i = bcIndex; i >= firstIndex && i > -1 ; i--) {
			locals.addAll(IRHelper.findLocalVariableIndex(ir, i, valueNumber));
		}

		return locals;
	}

	/**
	 * Tries to determine the position of the <code>load</code> instruction where the
	 * that loads the given local on to the stack, depending on the position of the local
	 * at the current stack.
	 *
	 * @param local the local to find the load for
	 * @param index the position of local at the stack
	 * @param stackSize the initial stack size
	 * @param bcIndex the bytecode index
	 *
	 * @return a pair (local, bytecode index), or <code>null</code> if no load was found
	 */
	public LocalInfo getLoadFor(int index, int stackSize, int bcIndex) {
		LOG.trace("getLoad(index={},stackSize={},bytecodeIndex={})", index, stackSize, bcIndex);

		StackSimulator locator = new StackSimulator(stackSize, index);

		SSACFG cfg = ir.getControlFlowGraph();
		ISSABasicBlock block = cfg.getBlockForInstruction(bcIndex);

		int start = bcIndex - 1;

		do {

			for (int i = start; i >= block.getFirstInstructionIndex(); i--) {
				IInstruction instruction = getBytecodeInstructions()[i];

				boolean found = locator.processBackward(instruction);

				if (found)  {
					return new LocalInfo(locator.getLocal(), i);
				}
			}

			if (cfg.getPredNodeCount(block) == 1) {
				block = cfg.getPredNodes(block).next();
				start = block.getLastInstructionIndex();
			} else {
				block = null;
			}

		} while(block != null);

		return null;

	}

	/**
	 * Tries to determine the position of the <code>store</code> instruction where the
	 * that loads the given local on to the stack, depending on the position of the local
	 * at the current stack.
	 *
	 * @param local the local to find the load for
	 * @param index the position of local at the stack
	 * @param stackSize the initial stack size
	 * @param bcIndex the bytecode index
	 *
	 * @return the bytecode index of the load instruction, or -1 if none was found
	 */
	public LocalInfo getStoreFor(int index, int stackSize, int bcIndex) {
		LOG.trace("getStore(index={},stackSize={},bytecodeIndex={})",index, stackSize, bcIndex);

		StackSimulator locator = new StackSimulator(stackSize, index);

		SSACFG cfg = ir.getControlFlowGraph();
		ISSABasicBlock block = cfg.getBlockForInstruction(bcIndex);

		int start = bcIndex + 1;

		do {

			int lastInstructionIndex = block.getLastInstructionIndex();
			for (int i = start; i <= lastInstructionIndex; i++) {
				IInstruction instruction = getBytecodeInstructions()[i];

				boolean found = locator.processForward(instruction);

				if (found)  {
					return new LocalInfo(locator.getLocal(), i);
				}
			}

			if (nodeCountWithoutExit(cfg, block) == 1) {
				block = cfg.getSuccNodes(block).next();
				start = block.getFirstInstructionIndex();
			} else {
				block = null;
			}

		} while(block != null);

		return null;
	}

	/**
	 * Named pair (local, bytecodeIndex)
	 *
	 */
	public class LocalInfo extends Pair<Integer, Integer> {
		protected LocalInfo(Integer fst, Integer snd) {
			super(fst, snd);
		}

		public int local() {
			return fst;
		}
		public int bcIndex() {
			return snd;
		}
	}

	private IInstruction[] getBytecodeInstructions() {
		if (bytecodeInstructions == null) {

			try {
				bytecodeInstructions = ((IBytecodeMethod)ir.getMethod()).getInstructions();
			} catch (InvalidClassFileException e) {
				throw new IllegalStateException(e);
			}
		}

		return bytecodeInstructions;
	}

	/**
	 * @return the value numbers of the param
	 */
	public Set<Integer> getParams() {
		return Sets.newHashSet(Ints.asList(ir.getParameterValueNumbers()));
	}

	/**
	 * @return the {@link SSAInstruction} array
	 */
	public SSAInstruction[] getInstructions() {
		return ir.getInstructions();
	}

	/**
	 * @param v a value number
	 * @return <code>true</code> if the given value number is a constant, <code>false</code> otherwise
	 */
	public boolean isConstant(int v) {
		return ir.getSymbolTable().isConstant(v);
	}

	/**
	 * @param v a value number representing a constant value
	 * @return the value of the constant
	 */
	public Object getConstantValue(int v) {
		return ir.getSymbolTable().getConstantValue(v);
	}

	/**
	 * @param v the value number to check
	 * @return the index in the parameter list of the given value number
	 */
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

	/**
	 * @return the maximum number of locals used by the method
	 */
	public int getMaxLocals() {
		return ((ShrikeCTMethod)ir.getMethod()).getMaxLocals();
	}


	private int findLastIndexBeforeBranch(int bcIndex) {
		SSACFG cfg = ir.getControlFlowGraph();

		ISSABasicBlock block = cfg.getBlockForInstruction(bcIndex);

		while (nodeCountWithoutExit(cfg, block) == 1) {
			block = cfg.getSuccNodes(block).next();
		}

		return block.getLastInstructionIndex();
	}

	private int nodeCountWithoutExit(SSACFG cfg, ISSABasicBlock block) {
		Iterator<ISSABasicBlock> iter = cfg.getSuccNodes(block);
		int count = 0;

		while (iter.hasNext()) {
			ISSABasicBlock next = iter.next();

			if (!next.isExitBlock()) {
				count++;
			}
		}


		return count;
	}

	private int findFirstIndexBeforeBranch(int bcIndex) {
		SSACFG cfg = ir.getControlFlowGraph();

		ISSABasicBlock block = cfg.getBlockForInstruction(bcIndex);

		while (cfg.getPredNodeCount(block) == 1) {
			block = cfg.getPredNodes(block).next();
		}

		return block.getFirstInstructionIndex();
	}

	/**
	 * Tries to determine the bytecode index of the first instruction where a phi definition is created for
	 * @param instruction the phi instruction to check for
	 * @return the bytecode index found
	 */
	public Integer getIndexForPhi(SSAPhiInstruction instruction) {
		if (phi2BlockMap == null) {
			initializePhiMap();
		}

		int def = instruction.getDef();

		BasicBlock basicBlock = ir.getControlFlowGraph().getBasicBlock(phi2BlockMap.get(def));
		return basicBlock.getFirstInstructionIndex();
	}

	private void initializePhiMap() {
		phi2BlockMap = Maps.newHashMap();
		SSACFG cfg = ir.getControlFlowGraph();

		for (int blockIndex = 0; blockIndex < cfg.getMaxNumber(); blockIndex++) {
			BasicBlock block = cfg.getBasicBlock(blockIndex);
			Iterator<SSAPhiInstruction> iter = block.iteratePhis();

			while (iter.hasNext()) {
				SSAPhiInstruction phi = iter.next();
				phi2BlockMap.put(phi.getDef(), blockIndex);
			}

		}
	}


	public Integer getConstantIndex(int v) {
		return ir.getSymbolTable().getIndex(v);
	}

	public ShrikeClass getDeclaringClass() {
		return (ShrikeClass) ir.getMethod().getDeclaringClass();

	}



}
