package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.ConstantInstruction;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class ConstantDefinitionConversionPatch extends PushCreateAndStoreToLocalConversionPatch {

	private ConstantInstruction createConstant;

	public ConstantDefinitionConversionPatch(TypeLabel label, int optLocal, Object value) {
		super(label, optLocal);
		createConstant = ConstantInstruction.makeString((String) value);
	}

	@Override
	protected void pushOriginalToStack(OutputBuilder w) {
		// TODO check which type is original type and use that instead
		w.emit(createConstant);

	}

}
