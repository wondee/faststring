package de.unifrankfurt.faststring.analysis.model;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public class ReturnUse extends Use {

	@Override
	public String toString() {
		return "ReturnUse [byteCodeIndex=" + getByteCodeIndex() + ", varIndex= " + getLocalVariableIndex()+ "]";
	}

	@Override
	public boolean isCompatibleWith(TypeLabel label) {
		return false;
	}


}
