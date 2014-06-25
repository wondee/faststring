package de.unifrankfurt.faststring.analysis;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import de.unifrankfurt.faststring.analysis.graph.Reference;

/**
 * Represents a result of a analysis
 * 
 * @author markus
 *
 */
public class AnalysisResult  {

	private Collection<Reference> refs;

	private int maxLocals;
	
	public AnalysisResult(Collection<Reference> refs) {
		init(refs);
	}

	
	
	private void init(Collection<Reference> refs) {
		this.refs = ImmutableSet.copyOf(refs);
	}

	public Collection<Reference> getRefs() {
		return refs;
	}

	public boolean isEmpty() {
		return refs.isEmpty();
	}

	public void setMaxLocals(int maxLocals) {
		this.maxLocals = maxLocals;
	}
	
	public int getMaxLocals() {
		return maxLocals;
	}
}
