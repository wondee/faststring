package de.unifrankfurt.faststring.analysis.model;

import java.util.Collection;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public interface PhiObject {

	void setLabel(TypeLabel label);
	
	TypeLabel getLabel();
	
	Collection<Integer> getConnctedRefs();
}
