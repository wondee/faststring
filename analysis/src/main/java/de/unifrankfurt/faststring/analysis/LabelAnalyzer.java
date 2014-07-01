package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.model.DataFlowObject;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.PhiObject;
import de.unifrankfurt.faststring.analysis.model.Use;
import de.unifrankfurt.faststring.analysis.util.GraphUtil;
import de.unifrankfurt.faststring.analysis.util.QueueUtil;
import de.unifrankfurt.faststring.analysis.util.QueueUtil.BaseQueueProcessingStrategy;
import de.unifrankfurt.faststring.analysis.util.StringUtil;

public class LabelAnalyzer extends BaseQueueProcessingStrategy<Reference>{

	private static final Logger LOG = LoggerFactory.getLogger(LabelAnalyzer.class);

	private final DataFlowGraph graph;

	private Collection<PhiObject> phis;
	
	
	public LabelAnalyzer(DataFlowGraph graph) {
		this.graph = graph;
		phis = Sets.newHashSet();
	}

	private Function<Integer, Reference> valueNumberToReference = new Function<Integer, Reference>() {
		@Override
		public Reference apply(Integer input) {
			return graph.get(input);
		}
	};
	

	public static void analyzeLabel(DataFlowGraph graph) {
		analyzeLabel(graph, graph.getAllLabelMatchingReferences());
	}
	
	public static void analyzeLabel(DataFlowGraph graph, Collection<Reference> refs) {
		if (refs.size() > 0) {
			QueueUtil.processUntilQueueIsEmpty(refs, new LabelAnalyzer(graph));
			
			Collection<Reference> finalRefs = graph.getAllLabelMatchingReferences();
			
			if (LOG.isDebugEnabled() && finalRefs.size() > 0) {
				LOG.debug("found possible references: {}", GraphUtil.extractIntsFromStringReferences(finalRefs));
				LOG.debug("definition conversion needed for: {}", StringUtil.toStringMap(GraphUtil.extractDefConversions(finalRefs)));
				LOG.debug("usage conversion needed for: {}", StringUtil.toStringMap(GraphUtil.extractUsageConversions(finalRefs)));
			}
			
		} else {
			LOG.debug("no references to analyze");
		}
	}


	@Override
	public void process(Reference ref, Queue<Reference> refQueue) {
		List<DataFlowObject> os = Lists.newLinkedList();

		os.add(ref.getDef());
		os.addAll(ref.getUses());

		for (DataFlowObject o : os) {
			
			if (o instanceof PhiObject) {
				PhiObject phi = (PhiObject) o;
				
				if (!phis.contains(phi)) {
					
					phis.add(phi);
					List<Integer> connectedRefs = o.getConnectedRefs(null);
					
					connectedRefs.remove(ref.getRef());
					Collection<Reference> refs = Collections2.transform(connectedRefs, 
							valueNumberToReference);
					
					refQueue.addAll(refs);
					
//					System.out.println(GraphUtil.extractIntsFromStringReferences(refQueue));
//					System.out.println(phis);
				}
			} else {
				if (o.isCompatibleWith(graph.getLabel())) {
					LOG.trace("inspecting {}", o);
					for (Integer connectedRefId : o.getConnectedRefs(graph.getLabel())) {
						Reference connectedRef = graph.get(connectedRefId);
						// TODO maybe somewhere else...
						if (connectedRef.getLabel() == null) {
							LOG.trace("setting label to {}", connectedRef);
							connectedRef.setLabel(graph.getLabel());

							refQueue.add(connectedRef);
						}
					}

				} 
			}
		}
	}

	@Override
	public void finished() {
		
		for (PhiObject phi : phis) {
			
			Multiset<TypeLabel> counter = HashMultiset.create();
			
			for (Integer v : phi.getConnctedRefs()) {
				counter.add(graph.get(v).getLabel());
			}
			
			int countNone = counter.count(null);
			int countLabel = counter.count(graph.getLabel());
			
			if (countLabel >= countNone) {
				phi.setLabel(graph.getLabel());
			}			
		}
		
		Collection<Reference> refs = graph.getReferences();
		for (Reference ref : refs) {
			List<DataFlowObject> list = Lists.<DataFlowObject>newLinkedList(ref.getUses());
			
			list.add(ref.getDef());
			
			if (ref.getLabel() != null) {
			
				for (DataFlowObject o : list) {
					if (!o.isCompatibleWith(graph.getLabel())) {
						if (o instanceof Definition) {
							ref.setDefinitionConvertToOpt();
						} else {
							ref.setConvertUseFromOpt((Use)o);
						}
					}
				}
			} else {
				for (DataFlowObject o : list) {
					if (!o.isCompatibleWith(null)) {
						if (o instanceof Definition) {
							ref.setDefinitionConvertFromOpt();
						} else {
							ref.setConvertUseToOpt((Use)o);
						}
					}
					
				}
			}
		}
	}
	

}
