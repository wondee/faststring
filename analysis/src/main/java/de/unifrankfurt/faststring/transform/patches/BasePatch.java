package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.Instruction;
import com.ibm.wala.shrikeBT.MethodEditor.Output;
import com.ibm.wala.shrikeBT.MethodEditor.Patch;

public abstract class BasePatch extends Patch {

	@Override
	public void emitTo(Output w) {
		createInstructions(new OutputBuilder(w));
	}

	protected abstract void createInstructions(OutputBuilder outputBuilder);

	class OutputBuilder {
		private Output w;

		public OutputBuilder(Output w) {
			this.w = w;
		}

		public OutputBuilder emit(Instruction ins) {
			return emit(new Instruction[] {ins});
		}

		public OutputBuilder emit(Instruction ... inss) {
			for (Instruction instruction : inss) {
				w.emit(instruction);
			}

			return this;
		}
	}

}
