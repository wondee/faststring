package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;
import java.util.Stack;

import com.ibm.wala.shrikeBT.ArrayLengthInstruction;
import com.ibm.wala.shrikeBT.IArrayLoadInstruction;
import com.ibm.wala.shrikeBT.IArrayStoreInstruction;
import com.ibm.wala.shrikeBT.IInstruction.Visitor;
import com.ibm.wala.shrikeBT.IInvokeInstruction;
import com.ibm.wala.shrikeBT.IStoreInstruction;

public class LocalVariableSolver extends Visitor {

	private static final int VALUE = -1;

	private Stack<Integer> stack = new Stack<>();
	private int toCheck;

	private boolean finished = false;

	public LocalVariableSolver(List<Integer> uses, int toCheck) {

		stack.push(toCheck);
		this.toCheck = toCheck;

	}


	public boolean isFinished() {
		return finished;
	}

	private void popFromStack(int poppedCount) {
		for (int i = 0; i < poppedCount && !stack.isEmpty(); i++) {
			stack.pop();
		}

		if (!stack.contains(toCheck)) {
			finished = true;
		}

	}

	@Override
	public void visitLocalStore(IStoreInstruction instruction) {
		if (stack.peek() == toCheck) {
//			possibleLocals.add(instruction.getVarIndex());
		}

		popFromStack(instruction.getPoppedCount());
	}

	@Override
	public void visitArrayLoad(IArrayLoadInstruction instruction) {
		stack.push(VALUE);
	}

	@Override
	public void visitArrayStore(IArrayStoreInstruction instruction) {
		popFromStack(instruction.getPoppedCount());
	}

	@Override
	public void visitArrayLength(ArrayLengthInstruction instruction) {
		popFromStack(instruction.getPoppedCount());
		stack.push(VALUE);
	}


	@Override
	public void visitInvoke(IInvokeInstruction instruction) {
		popFromStack(instruction.getPoppedCount());
		stack.push(VALUE);

		super.visitInvoke(instruction);
	}

}
