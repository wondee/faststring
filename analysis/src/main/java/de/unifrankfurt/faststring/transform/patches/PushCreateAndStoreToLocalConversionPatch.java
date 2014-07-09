package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.StoreInstruction;
import com.ibm.wala.shrikeBT.Util;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public abstract class PushCreateAndStoreToLocalConversionPatch extends ConversationToLabelPatch {
	
	private final StoreInstruction storeToOptLocal;

	public PushCreateAndStoreToLocalConversionPatch(TypeLabel label, int optLocal) {
		super(label);
		
		storeToOptLocal = StoreInstruction.make(Util.makeType(optimizedType), optLocal);
	}


	@Override
	protected void createInstructions(OutputBuilder w) {
		pushOriginalToStack(w);
		w.emit(
			invokeStaticFactory,
			storeToOptLocal
		);
	}


	protected abstract void pushOriginalToStack(OutputBuilder w);

}