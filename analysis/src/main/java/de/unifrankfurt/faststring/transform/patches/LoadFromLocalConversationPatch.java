package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.LoadInstruction;
import com.ibm.wala.shrikeBT.Util;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class LoadFromLocalConversationPatch extends PushCreateAndStoreToLocalConversionPatch {

	private LoadInstruction loadOriginalToStack;

	public LoadFromLocalConversationPatch(TypeLabel label, int orgLocal, int optLocal) {
		super(label, optLocal);
		loadOriginalToStack = LoadInstruction.make(Util.makeType(originalType), orgLocal);
	}

	@Override
	protected void pushOriginalToStack(OutputBuilder w) {
		w.emit(loadOriginalToStack);
	}


}
