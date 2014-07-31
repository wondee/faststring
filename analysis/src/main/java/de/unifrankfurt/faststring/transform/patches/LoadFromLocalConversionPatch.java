package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.LoadInstruction;
import com.ibm.wala.shrikeBT.Util;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class LoadFromLocalConversionPatch extends PushCreateAndStoreToLocalConversionPatch {

	private LoadInstruction loadOriginalToStack;

	public LoadFromLocalConversionPatch(TypeLabel from, TypeLabel label, int orgLocal, int optLocal) {
		super(from, label, optLocal);
		loadOriginalToStack = LoadInstruction.make(Util.makeType(originalType), orgLocal);
	}

	@Override
	protected void pushOriginalToStack(OutputBuilder w) {
		w.emit(loadOriginalToStack);
	}


}
