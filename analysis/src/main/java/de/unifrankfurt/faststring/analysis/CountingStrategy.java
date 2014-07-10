package de.unifrankfurt.faststring.analysis;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import de.unifrankfurt.faststring.analysis.LabelAnalyzer.PhiLabelingStrategy;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.PhiNode;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class CountingStrategy implements PhiLabelingStrategy {

	@Override
	public boolean shouldBeLabeled(DataFlowGraph graph, PhiNode phi) {
		
		Multiset<TypeLabel> counter = HashMultiset.create();
		
		for (Integer v : phi.getConnctedRefs()) {
			counter.add(graph.get(v).getLabel());
		}
		
		int countNone = counter.count(null);
		int countLabel = counter.count(graph.getLabel());
		
		return countLabel >= countNone;
	}

}
