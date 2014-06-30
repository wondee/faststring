package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.ConstantInstruction;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.transform.TransformationInfo.Constant;

public class ConstantDefinitionConversionPatch extends
		DefinitionConversionPatch {

	private Object value;

	public ConstantDefinitionConversionPatch(TypeLabel label, Constant constant) {
		super(label, constant.getLocal());
		this.value = constant.getValue();
	}

	@Override
	protected void pushOriginalToStack(OutputBuilder w) {
		// TODO check which type is original type and use that instead
		w.emit(ConstantInstruction.makeString((String) value));

	}

}
