package de.unifrankfurt.faststring.analysis;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraphBuilder;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class MethodAnalyzer {

	private static final Logger LOG = LoggerFactory
			.getLogger(MethodAnalyzer.class);

	private IRMethod method;

	private TypeLabel label;

	public MethodAnalyzer(IRMethod m, TypeLabel label) {
		this.method = m;
		this.label = label;
	}

	public AnalysisResult analyze() {
		DataFlowGraph graph = new DataFlowGraphBuilder(label, method).createDataFlowGraph();
		
		
		LabelAnalyzer.analyzeLabel(graph);
		
		
		Collection<Reference> refs = graph.getAllLabelMatchingReferences();

//		calculateMissingLocals(refs);

		AnalysisResult analysisResult = new AnalysisResult(refs, method.getMaxLocals(), label);

		return analysisResult;
	}

//	private void calculateMissingLocals(Collection<Reference> refs) {
//
//
//		for (Reference ref : refs) {
//
////			Set<Integer> phiPointer = method.findAllUsesPhiPointer(ref.getRef());
//
//			LOG.trace("checking for {} ", ref);
//
//			Set<Integer> newLocals = Sets.newHashSet(ref.getDef().getLocalVariableIndex());
//
//			for (Use  use : ref.getUses()) {
//
//
//
//				Collection<Integer> locals = use.getLocalVariableIndex();
//				LOG.trace("use: {}", locals);
//
//				for (Integer local : locals) {
//					if (!newLocals.contains(local)) {
//						System.out.println("neuen gefunden! " + local);
//						newLocals.add(local);
//					}
//				};
//			}
//
//			ref.getDef().setLocalVariableIndices(newLocals);
//		}
//
//	}

}
