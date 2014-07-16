package de.unifrankfurt.faststring.analysis.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.ibm.wala.shrikeBT.IInstruction;
import com.ibm.wala.shrikeBT.LoadInstruction;

public class LoadLocator {

	private static final Logger LOG = LoggerFactory.getLogger(LoadLocator.class);

	private int size;
	private int offset;
	private int local;


	public LoadLocator(int size, int index, int local) {

		Preconditions.checkArgument(index > -1);
		Preconditions.checkArgument(size > 1);
		Preconditions.checkArgument(index < size);

		this.offset = index;
		this.size = size;
		this.local = local;

	}


	public boolean process(IInstruction instruction) {
		LOG.trace("processing: {}", instruction);
		if (instruction.getPushedWordSize() > 0) {
			size--;
			if (offset == size - 1) {
				if (instruction instanceof LoadInstruction) {
					LoadInstruction load = (LoadInstruction) instruction;

					if (load.getVarIndex() == local) {
						LOG.trace("found load");

						return true;
					} else {
						throw new IllegalStateException("excpected local was " + local + ", but was " + load.getVarIndex());
					}
				} else {
					throw new IllegalStateException("instruction is no load: " + instruction);
				}
			}
		}
		int poppedCount = instruction.getPoppedCount();
		if (poppedCount > 0) {
			size += poppedCount;
		}

		LOG.trace("stack size={}", size);

		return false;
	}


}
