package de.unifrankfurt.faststring.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.ibm.wala.shrikeBT.DupInstruction;
import com.ibm.wala.shrikeBT.IInstruction;
import com.ibm.wala.shrikeBT.LoadInstruction;
import com.ibm.wala.shrikeBT.StoreInstruction;

public class StackSimulator {

	private static final Logger LOG = LoggerFactory.getLogger(StackSimulator.class);

	private int size;
	private int offset;
	private int local = -1;
	
	private boolean found = false;
	
	private boolean finished = false;
	
	public StackSimulator(int size, int index) {
		Preconditions.checkArgument(index > -1);
		Preconditions.checkArgument(size > 0);
		Preconditions.checkArgument(index < size);

		this.offset = index;
		this.size = size;
	}

	public boolean processBackward(IInstruction instruction) {
		LOG.trace("processing: {}", instruction);
		if (getPushedSize(instruction) > 0) {
			size--;
			if (offset == size) {
				if (offset == size) {
					if (instruction instanceof LoadInstruction) {
						LoadInstruction load = (LoadInstruction) instruction;
						local = load.getVarIndex(); 
						return true;
					} else {
						finished = true;
					}
				}
			}
			
		}
		
		size += getPoppedCount(instruction);
		LOG.trace("stack size={}", size);

		return found;
	}
	
	public boolean processForward(IInstruction instruction) {
		
		if (getPoppedCount(instruction) > 0) {
			size -= getPoppedCount(instruction);
			
			if (size == offset) {
				if (instruction instanceof StoreInstruction) {
					StoreInstruction store = (StoreInstruction) instruction;
					
					local = store.getVarIndex();
					return true;
				} else {
					finished = true;
				}
			}

		}
		
		if (getPushedSize(instruction) > 0) {
			size++;	
		}

		return found;
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
	
	public boolean isFinished() {
		return finished;
	}

}
