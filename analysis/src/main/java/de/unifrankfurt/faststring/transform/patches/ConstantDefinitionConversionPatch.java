package de.unifrankfurt.faststring.transform.patches;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class ConstantDefinitionConversionPatch extends
		DefinitionConversionPatch {

	public ConstantDefinitionConversionPatch(TypeLabel label, int optLocal) {
		super(label, optLocal);
		// TODO Auto-generated constructor stub
	}

	private Object value;

	@Override
	protected void pushOriginalToStack(OutputBuilder w) {
		// TODO Auto-generated method stub
		
	}

//	public ConstantDefinitionConversionPatch(TypeLabel label, Constant constant) {
//		super(label, constant.getLocal());
//		this.value = constant.getValue();
//	}
//
//	@Override
//	protected void pushOriginalToStack(OutputBuilder w) {
//		// TODO check which type is original type and use that instead
//		w.emit(ConstantInstruction.makeString((String) value));
//
//	}

}
