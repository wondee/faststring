package de.unifrankfurt.faststring.analysis;

import de.unifrankfurt.faststring.analysis.LabelAnalyzer.PhiLabelingStrategy;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.PhiNode;

public class OptimisticStrategy implements PhiLabelingStrategy {

	@Override
	public boolean shouldBeLabeled(DataFlowGraph graph, PhiNode phi) {
		for (Integer v : phi.getConnctedRefs()) {
			if (graph.get(v).getLabel() != null) return true;
		}
		
		return false;
	}

}
