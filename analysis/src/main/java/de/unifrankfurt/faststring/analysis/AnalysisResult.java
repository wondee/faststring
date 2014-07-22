package de.unifrankfurt.faststring.analysis;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import de.unifrankfurt.faststring.analysis.graph.Reference;

/**
 * Represents a result of a analysis
 * <p>
 * The following information is contained:
 * <ul>
 * <li>All {@link Reference}s which are labeled during the analysis</li>
 * <li>The analyzed method's name</li>
 * <li>The maximum used local of this method</li>
 * </ul>
 *
 */
public class AnalysisResult  {

	private Collection<Reference> refs;

	private int maxLocals;

	private String methodName;

	public AnalysisResult(Collection<Reference> refs, int maxLocals, String methodName) {
		this.refs = ImmutableSet.copyOf(refs);
		this.maxLocals = maxLocals;
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

	public String getMethodName() {
		return methodName;
	}
}
