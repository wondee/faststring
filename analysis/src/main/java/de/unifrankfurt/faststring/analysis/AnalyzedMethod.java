package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.ShrikeCTMethod;
import com.ibm.wala.shrikeBT.IInstruction;
import com.ibm.wala.shrikeBT.LoadInstruction;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSACFG.BasicBlock;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;

import de.unifrankfurt.faststring.analysis.util.IRUtil;
import de.unifrankfurt.faststring.analysis.util.QueueUtil;
import de.unifrankfurt.faststring.analysis.util.QueueUtil.BaseQueueProcessingStrategy;

public class AnalyzedMethod {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(AnalyzedMethod.class);

	private IR ir;
	private DefUse defUse;

	private Map<SSAInstruction, Integer> instructionToIndexMap;

	private Map<Integer, Integer> phi2BlockMap;

	private IInstruction[] bytecodeInstructions;

	public AnalyzedMethod(IR ir, DefUse defUse) {
		this.ir = ir;
		this.defUse = defUse;
	}

	public Integer getIndexFor(SSAInstruction instruction) {
		
		if (instruction instanceof SSAPhiInstruction) {
			return getIndexForPhi(instruction);
		} else {
			if (instructionToIndexMap == null) {
				instructionToIndexMap = IRUtil.createInstructionToIndexMap(ir);
			}
			return instructionToIndexMap.get(instruction);
		}

	}

	public String getMethodName() {
		return ir.getMethod().getName().toString();
	}

	public SSAInstruction getDef(int v) {
		return defUse.getDef(v);
	}

	public Iterator<SSAInstruction> getUses(int v) {
		return defUse.getUses(v);
	}

	public Collection<Integer> getLocalVariableIndices(int bcIndex, int valueNumber) {

		int lastIndex = findLastIndexBeforeBranch(bcIndex);

		Set<Integer> pointers = IRUtil.findAllDefPointersFor(defUse, valueNumber);

		Set<Integer> locals = Sets.newHashSet();

		for (Integer p : pointers) {
			for (int i = bcIndex + 1; i < lastIndex; i++) {
				locals.addAll(IRHelper.findLocalVariableIndex(ir, i, p));
			}
		}

		return locals;

	}

	/**
	 * assumption: the load of this local is the first one that appears when searching backwards the cfg
	 * 
	 * @param local the local that is loaded
	 * @param bcIndex the bytecode index where this local is used
	 * @return 
	 */
	public int getLoadFor(int local, int bcIndex) {

		
		SSACFG cfg = ir.getControlFlowGraph();
		
		ISSABasicBlock block = cfg.getBlockForInstruction(bcIndex);
		
		int first = block.getFirstInstructionIndex();
		
		int loadIndex = checkBlockForLoad(local, bcIndex, first);
		
		if (loadIndex == -1) {
			CFGTraversingStrategy strategy = new CFGTraversingStrategy(cfg, local);
			QueueUtil.processUntilQueueIsEmpty(cfg.getPredNodes(block), strategy);
			
			loadIndex = strategy.getLoadIndex();
			
			if (loadIndex == -1) {
				throw new IllegalStateException(String.format("no load found for local %d from bytecode index %d", local, bcIndex));
			}
		}
		
		return loadIndex;
	}

	class CFGTraversingStrategy extends BaseQueueProcessingStrategy<ISSABasicBlock> {
		private int local;
		private int loadIndex = -1;
		private SSACFG cfg;

		public CFGTraversingStrategy(SSACFG cfg, int local) {
			this.cfg = cfg;
			this.local = local;
		}
		
		@Override
		public void process(ISSABasicBlock block, Queue<ISSABasicBlock> queue) {
			int found = checkBlockForLoad(local, block.getLastInstructionIndex(), block.getFirstInstructionIndex());
			if (found != -1) {
				setLoadIndex(found);
			}
			if (loadIndex == -1) {
				queue.addAll(Lists.newArrayList(cfg.getPredNodes(block)));
			}
			
		}
		
		private void setLoadIndex(int loadIndex) {
			if (this.loadIndex != loadIndex) {
				if (this.loadIndex == -1) {
					this.loadIndex = loadIndex;
				} else {
					throw new IllegalStateException(String.format("two loads where found %d and %d ", loadIndex, this.loadIndex));
				}
			}
		}
		
		public int getLoadIndex() {
			return loadIndex;
		}
	}
	
		
	private int checkBlockForLoad(int local, int bcIndex, int first) {
		IInstruction[] bcInstructions = getBytecodeInstructions();
		for (int i = bcIndex; i >= first; i--) {
			if (bcInstructions[i] instanceof LoadInstruction) {
				LoadInstruction load = (LoadInstruction) bcInstructions[i];
				
				if (load.getVarIndex() == local) {
					return i;
				}
			}
		}
		return -1;
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

//	public int getByteCodeIndexFor(Integer index) {
//		try {
//			IBytecodeMethod method = (IBytecodeMethod) ir.getMethod();
//			return method.getBytecodeIndex(index);
//		} catch (InvalidClassFileException e) {
//			throw new IllegalStateException("could not read class file", e);
//		}
//	}

	public int getMaxLocals() {
		return ((ShrikeCTMethod)ir.getMethod()).getMaxLocals();
	}


//	public IInstruction[] getLastIndexOfSinglePathReachedBlock(int bcIndex) {
//		try {
//
//			int from = bcIndex + 1;
//			int to = findLastIndexBeforeBranch(ir.getInstructions()[bcIndex]);
//
//			if (from < to) {
//				return Arrays.copyOfRange(
//						((IBytecodeMethod)ir.getMethod()).getInstructions(), from, to);
//			} else {
//				return new IInstruction[0];
//			}
//		} catch (InvalidClassFileException e) {
//			throw new IllegalStateException(e);
//		}
//
//
//	}

	private int findLastIndexBeforeBranch(int bcIndex) {		
		SSACFG cfg = ir.getControlFlowGraph();

		ISSABasicBlock block = cfg.getBlockForInstruction(bcIndex);
		
		while (cfg.getSuccNodeCount(block) == 2) {
			block = cfg.getSuccNodes(block).next();
		}

		return block.getLastInstructionIndex();
	}

	public Integer getIndexForPhi(SSAInstruction instruction) {
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

//	public int findConstantBytecodeIndex(int v, SSAInstruction instruction) {
//		try {
//
//			List<Integer> uses = Lists.reverse(IRUtil.getUses(instruction));
//			
//			int constant = IRUtil.findUseIndex(v, uses);
//			
//			int last = getIndexFor(instruction);
//			int first = findFirstIndexBeforeBranch(last);
//		
//			IInstruction[] instructions = ((IBytecodeMethod)ir.getMethod()).getInstructions();
//			
//			new LocalVariableSolver(uses, constant);
//			
//			for (int i = last; i >= first; i--) {
//				IInstruction iInstruction = instructions[i];
////				iInstruction.visit(v);
//			}
//			
//			
//			return 0;
//		} catch (InvalidClassFileException e) {
//			throw new IllegalStateException(e);
//		}
//		
//	}

	public Integer getConstantIndex(int v) {
		return ir.getSymbolTable().getIndex(v);
	}


}
