package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.LoadInstruction;
import com.ibm.wala.shrikeBT.Util;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class OptConversationPatch extends DefinitionConversionPatch {

	private int orgLocal;

	public OptConversationPatch(TypeLabel label, int orgLocal, int optLocal) {
		super(label, optLocal);
		this.orgLocal = orgLocal;
	}

	@Override
	protected void pushOriginalToStack(OutputBuilder w) {
		w.emit(LoadInstruction.make(Util.makeType(label.getOriginalType()), orgLocal));
	}


}
