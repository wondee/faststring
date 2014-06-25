package de.unifrankfurt.faststring.analysis;

import java.util.Collection;

import de.unifrankfurt.faststring.analysis.graph.Reference;

public class MethodAnalyzer {
	
	private IRMethod method;
	private LabelAnalyzer analyzer;
	
	public MethodAnalyzer(IRMethod m, LabelAnalyzer analyzer) {
		this.method = m;
		this.analyzer = analyzer;
	}
	
	public AnalysisResult analyze() {
		Collection<Reference> refs = analyzer.analyzeLabel(method);
		
		AnalysisResult analysisResult = new AnalysisResult(refs);
		
		
		analysisResult.setMaxLocals(method.getMaxLocals());
		
		return analysisResult;
	}
	
}
