package de.unifrankfurt.faststring.analysis;

import java.util.Iterator;
import java.util.List;

import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.util.graph.traverse.BFSPathFinder;

public class IRAnalyzer {

	private IR ir;

	public IRAnalyzer(IR ir) {
		this.ir = ir;
	}

	/**
	 * checks if there is a way in the control flow graph from the given source instruction 
	 * to the given {@link SSAInstruction}.
	 * 
	 * @param src the source instruction index
	 * @param targetIns the target instruction
	 * @return true if there is a way to the target, false otherwise
	 */
	public boolean isConnected(int src, SSAInstruction targetIns) {
		int target = findIndexForInstruction(targetIns);
		
		if (target < 0) {
			ISSABasicBlock srcBlock = ir.getControlFlowGraph().getBlockForInstruction(src);
			ISSABasicBlock targetBlock = ir.getBasicBlockForInstruction(targetIns);
			
			return isConnected(srcBlock, targetBlock);
		}
		
		return isConnected(src, target);
	}
	
	/**
	 * checks if there is a way in the control flow graph from the given source instruction 
	 * to the given target instruction. Both are identified by their instruction index in the given {@link IR}.
	 * 
	 * @param src the source instruction index
	 * @param target the target instruction index
	 * @return {@code true} if there is a way to the target, {@code false} otherwise
	 */
	public boolean isConnected(int src, int target) {
		SSACFG graph = ir.getControlFlowGraph();

		ISSABasicBlock srcBlock = graph.getBlockForInstruction(src);
		ISSABasicBlock targetBlock = graph.getBlockForInstruction(target);

		// src is in the same block as the target instruction and it is evaluated after src 
		if (srcBlock.equals(targetBlock) && src < target) {
			return true;
		} else {
			return isConnected(srcBlock, targetBlock);
		}

	}
	
	public boolean isConnected(ISSABasicBlock src, ISSABasicBlock target) {
		SSACFG graph = ir.getControlFlowGraph();
	
		Iterator<ISSABasicBlock> succNodes = graph.getSuccNodes(src);
		
		BFSPathFinder<ISSABasicBlock> pathFinder = new BFSPathFinder<ISSABasicBlock>(
				graph, 
				succNodes, 
				target);
		
		List<ISSABasicBlock> path = pathFinder.find();
		
		return path != null;
		
	}
	
	
	public int findIndexForInstruction(SSAInstruction target) {
		for (int i = 0; i < ir.getInstructions().length; i++) {
			if (target == ir.getInstructions()[i]) {
				return i;
			}
		}
		
		return -1;
	}


}
