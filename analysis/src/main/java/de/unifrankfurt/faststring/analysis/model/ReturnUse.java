package de.unifrankfurt.faststring.analysis.model;

import de.unifrankfurt.faststring.analysis.label.StringTypeLabel;


public class ReturnUse extends Use {

	@Override
	public String toString() {
		return "ReturnUse";
	}

	@Override
	public boolean isCompatibleWith(StringTypeLabel label) {
		return false;
	}


}
