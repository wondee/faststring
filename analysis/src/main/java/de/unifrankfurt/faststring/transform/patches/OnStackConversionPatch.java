package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.DupInstruction;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class OnStackConversionPatch extends PushCreateAndStoreToLocalConversionPatch {

	private DupInstruction dup;

	public OnStackConversionPatch(TypeLabel from, TypeLabel label, int optLocal) {
		super(from, label, optLocal);
		dup = DupInstruction.make(0);
	}

	@Override
	protected void pushOriginalToStack(OutputBuilder w) {
		w.emit(dup);
	}

}
