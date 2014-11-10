package de.unifrankfurt.faststring.analysis.graph;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

/**
 * Represents a Node in the {@link DataFlowGraph} that a
 *
 * @author markus
 *
 */
public interface Labelable {

	void setLabel(TypeLabel label);

	TypeLabel getLabel();

	boolean isLabel(TypeLabel label);

	boolean isSameLabel(Labelable other);

}