package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;
import java.util.Stack;

import com.google.common.collect.Lists;
import com.ibm.wala.shrikeBT.ConstantInstruction;
import com.ibm.wala.shrikeBT.DupInstruction;
import com.ibm.wala.shrikeBT.IInvokeInstruction;
import com.ibm.wala.shrikeBT.ILoadInstruction;
import com.ibm.wala.shrikeBT.IInstruction.Visitor;

public class LoadLocator extends Visitor {

	private final Stack<Object> stack;
	private int positionOfCheck;

	public LoadLocator(List<Integer> initialStack, int index) {
		stack = new Stack<>();

		for (int v : Lists.reverse(initialStack)) {
			stack.push(v);
		}

		positionOfCheck = index;
	}

	private void removeFromStack() {
		stack.pop();
	}

	@Override
	public void visitConstant(ConstantInstruction instruction) {
		removeFromStack();
		;
	}


	@Override
	public void visitInvoke(IInvokeInstruction instruction) {
		super.visitInvoke(instruction);
	}

	@Override
	public void visitDup(DupInstruction instruction) {
		super.visitDup(instruction);
	}

	@Override
	public void visitLocalLoad(ILoadInstruction instruction) {
		super.visitLocalLoad(instruction);
	}

}
