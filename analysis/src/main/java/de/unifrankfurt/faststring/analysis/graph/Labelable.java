package de.unifrankfurt.faststring.analysis.graph;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public interface Labelable {

	public abstract void setLabel(TypeLabel label);

	public abstract TypeLabel getLabel();

	public abstract boolean isLabel(TypeLabel label);

}