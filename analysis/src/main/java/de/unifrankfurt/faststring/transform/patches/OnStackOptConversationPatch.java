package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.DupInstruction;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class OnStackOptConversationPatch extends DefinitionConversionPatch {

	public OnStackOptConversationPatch(TypeLabel label, int optLocal) {
		super(label, optLocal);
	}

	@Override
	protected void pushOriginalToStack(OutputBuilder w) {
		w.emit(DupInstruction.make(0));

	}

}
