package de.unifrankfurt.faststring.analysis.model;

import de.unifrankfurt.faststring.analysis.label.Label;


public class ReturnUse extends Use {

	@Override
	public String toString() {
		return "ReturnUse";
	}

	@Override
	public boolean isCompatibleWith(Label label) {
		return false;
	}


}
