package de.unifrankfurt.faststring.analysis.graph;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public interface Labelable {

	abstract void setLabel(TypeLabel label);

	abstract TypeLabel getLabel();

	boolean isLabel(TypeLabel label);
	
	boolean isSameLabel(Labelable other);

}