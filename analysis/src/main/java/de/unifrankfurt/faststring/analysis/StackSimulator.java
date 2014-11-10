package de.unifrankfurt.faststring.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.ibm.wala.shrikeBT.DupInstruction;
import com.ibm.wala.shrikeBT.IInstruction;
import com.ibm.wala.shrikeBT.LoadInstruction;
import com.ibm.wala.shrikeBT.StoreInstruction;

/**
 * Simulates the bytecode stack to check where a given value on the stack has been
 * loaded or stored.
 * <p>
 * Given a size of a stack and an index of the value you are interested in, this class
 * keeps track when the certain value leaves the stack. So this given instruction which
 * causes this value leave must be responsible to store or load this value.
 *
 * @author markus
 *
 */
public class StackSimulator {

	private static final Logger LOG = LoggerFactory.getLogger(StackSimulator.class);

	private int size;
	private int offset;
	private int local = -1;

	public StackSimulator(int size, int index) {
		Preconditions.checkArgument(index > -1);
		Preconditions.checkArgument(size > 0);
		Preconditions.checkArgument(index < size);

		this.offset = index;
		this.size = size;
	}

	/**
	 * simulates the proccessing of the given instruction
	 *
	 * @param instruction the instruction to process
	 * @return <code>true</code> if the value has left the stack, <code>false</code> otherwise
	 */
	public boolean processBackward(IInstruction instruction) {
		LOG.trace("processing: {}", instruction);
		if (getPushedSize(instruction) > 0) {
			size--;
			if (offset == size) {
				if (instruction instanceof LoadInstruction) {
					LoadInstruction load = (LoadInstruction) instruction;
					local = load.getVarIndex();
				}
				return true;
			}

		}

		size += getPoppedCount(instruction);
		LOG.trace("stack size={}", size);

		return false;
	}

	public boolean processForward(IInstruction instruction) {

		if (getPoppedCount(instruction) > 0) {
			size -= getPoppedCount(instruction);
			if (size == offset) {
				if (instruction instanceof StoreInstruction) {
					StoreInstruction store = (StoreInstruction) instruction;

					local = store.getVarIndex();
				}
				return true;
			}

		}

		if (getPushedSize(instruction) > 0) {
			size++;
		}

		return false;
	}

	private byte getPushedSize(IInstruction instruction) {
		return (instruction instanceof DupInstruction) ? 1 : instruction.getPushedWordSize();
	}

	private int getPoppedCount(IInstruction instruction) {
		return (instruction instanceof DupInstruction) ? 0 : instruction.getPoppedCount();
	}

	public int getLocal() {
		return local;
	}

}
