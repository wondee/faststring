package de.unifrankfurt.faststring.analysis;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

import de.unifrankfurt.faststring.analysis.LabelAnalyzer.PhiLabelingStrategy;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.PhiNode;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class CountingStrategy implements PhiLabelingStrategy {

	@Override
	public TypeLabel shouldBeLabeled(DataFlowGraph graph, PhiNode phi) {
		
		Multiset<TypeLabel> counter = HashMultiset.create();
		
		for (Integer v : phi.getConnctedRefs()) {
			counter.add(graph.get(v).getLabel());
		}
		
		int max = -1;
		TypeLabel maxLabel = null;
		
		for (Entry<TypeLabel> e : counter.entrySet()) {
			if (e.getCount() > max || (e.getCount() == max && e.getElement() != null)) {
				max = e.getCount();
				maxLabel = e.getElement();
			}
		}
		
		return maxLabel;
	}

}
