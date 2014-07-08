package de.unifrankfurt.faststring.analysis;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;

/**
 * Represents a result of a analysis
 *
 * @author markus
 *
 */
public class AnalysisResult  {

	private Collection<Reference> refs;

	private int maxLocals;

	private TypeLabel label;

	private String methodName;

	public AnalysisResult(Collection<Reference> refs, int maxLocals, TypeLabel typeLabel, String methodName) {
		this.refs = ImmutableSet.copyOf(refs);
		this.maxLocals = maxLocals;
		this.label = typeLabel;
		this.methodName = methodName;
	}



	public Collection<Reference> getRefs() {
		return refs;
	}

	public boolean isEmpty() {
		return refs.isEmpty();
	}

	public int getMaxLocals() {
		return maxLocals;
	}

	public TypeLabel getLabel() {
		return label;
	}

	public String getMethodName() {
		return methodName;
	}
}
