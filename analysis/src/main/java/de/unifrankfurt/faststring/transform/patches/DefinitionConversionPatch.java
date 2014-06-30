package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.StoreInstruction;
import com.ibm.wala.shrikeBT.Util;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public abstract class DefinitionConversionPatch extends BasePatch {

	TypeLabel label;

	private int optLocal;

	public DefinitionConversionPatch(TypeLabel label, int optLocal) {
		this.label = label;
		this.optLocal = optLocal;
	}


	@Override
	protected void createInstructions(OutputBuilder w) {
		pushOriginalToStack(w);
		w.emit(Util.makeInvoke(label.getOptimizedType(), label.getCreationMethodName(), new Class<?>[]{label.getOriginalType()}))
			.emit(StoreInstruction.make(Util.makeType(label.getOptimizedType()), optLocal));
	}


	protected abstract void pushOriginalToStack(OutputBuilder w);

}