package de.unifrankfurt.faststring.analysis.graph;

import java.util.List;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public abstract class LabelableNode extends InstructionNode implements Labelable {

	private TypeLabel label;
	
	
	public boolean needsConversionTo(Reference ref) {
		if (isSameLabel(ref)) {
			return false;
		}

		if (ref.getLabel() != null) {
			return !ref.getLabel().compatibleWith(label);
		} else {
			return !label.compatibleWith(ref.getLabel());
		}
		
	}

	public abstract boolean canProduce(TypeLabel label);
	
	public abstract List<Integer> getLabelableRefs(TypeLabel label);

	public abstract boolean canUseAt(TypeLabel label, int i);
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.unifrankfurt.faststring.analysis.graph.Labelable#setLabel(de.unifrankfurt
	 * .faststring.analysis.label.TypeLabel)
	 */
	@Override
	public void setLabel(TypeLabel label) {
		this.label = label;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.unifrankfurt.faststring.analysis.graph.Labelable#getLabel()
	 */
	@Override
	public TypeLabel getLabel() {
		return label;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.unifrankfurt.faststring.analysis.graph.Labelable#isLabel(de.unifrankfurt
	 * .faststring.analysis.label.TypeLabel)
	 */
	@Override
	public boolean isLabel(TypeLabel label) {
		return this.label == label;
	}

	@Override
	public boolean isSameLabel(Labelable other) {
		return isLabel(other.getLabel());
	}
}
