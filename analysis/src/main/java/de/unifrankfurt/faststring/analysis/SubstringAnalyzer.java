package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.ibm.wala.classLoader.IMethod;

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraphBuilder;
import de.unifrankfurt.faststring.analysis.graph.GraphUtil;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.BuiltInTypes;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.model.DataFlowObject;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;
import de.unifrankfurt.faststring.analysis.util.StringUtil;
import de.unifrankfurt.faststring.analysis.util.UniqueQueue;

public class SubstringAnalyzer {

	private static final Logger LOG = LoggerFactory.getLogger(SubstringAnalyzer.class);
		
	private IRMethod ir;

	private TypeLabel label = BuiltInTypes.SUBSTRING;

	private DataFlowGraph graph;

	
	public SubstringAnalyzer(TargetApplication targetApplication, IMethod m) {
		ir = targetApplication.findIRMethodForMethod(m);
		
	}
	
	
	public Set<Reference> findCandidates() {
		LOG.info("analyzing (for {}) Method: {}", label, ir.getMethodSignature());
		
		graph = getGraph();
		Collection<Reference> refs = graph.getAllLabelMatchingReferences();
		
		if (refs.size() > 0) {
			processUntilQueueIsEmpty(refs, definitionCheck);
			processUntilQueueIsEmpty(graph.getAllLabelMatchingReferences(), usageCheck);
			
			
		} else {
			LOG.debug("no string uses found");
		}
		
		Collection<Reference> finalRefs = graph.getAllLabelMatchingReferences();
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("found possible references: {}", GraphUtil.extractIntsFromStringReferences(finalRefs));
			LOG.debug("definition conversion needed for: {}", StringUtil.toStringMap(GraphUtil.extractDefConversions(finalRefs)));
			LOG.debug("usage conversion needed for: {}", StringUtil.toStringMap(GraphUtil.extractUsageConversions(finalRefs)));
		}
		
		return Sets.newHashSet(finalRefs);
		
	}


	private DataFlowGraph getGraph() {
		if (graph == null) {
			graph = new DataFlowGraphBuilder(label, ir).createDataFlowGraph();
		}
		return graph;
	}


	private void processUntilQueueIsEmpty(Collection<Reference> contents, CheckingStrategy strategy) {
		Queue<Reference> refQueue = new UniqueQueue<>(contents);
		
		while(!refQueue.isEmpty()) {
			strategy.checkReference(refQueue.remove(), refQueue);
		}
		
	}

	private interface CheckingStrategy {
		void checkReference(Reference ref, Queue<Reference> refQueue);
	}
	
	private CheckingStrategy definitionCheck = new CheckingStrategy() {
		@Override
		public void checkReference(Reference ref, Queue<Reference> refQueue) {
			
			
			Definition def = ref.getDef();
			if (def.isCompatibleWith(label)) {
				check(def, refQueue);
				
			} else {
				ref.setConvertToDefinition();
			}
			
		}
	};
	
	private CheckingStrategy usageCheck = new CheckingStrategy() {
		@Override
		public void checkReference(Reference ref, Queue<Reference> refQueue) {
			List<Use> uses = ref.getUses();
			for (int useId = 0; useId < uses.size(); useId++) {
				Use use = uses.get(useId);
				if (use.isCompatibleWith(label)) {
					check(use, refQueue);
				} else {
					ref.setConvertToUse(useId);
					
				}
			}
			
		}
	};
	
	

	private void check(DataFlowObject o, Queue<Reference> refQueue) {		
		LOG.debug("inspecting {}", o);
		for (Integer connectedRefId : o.getConnectedRefs(label)) {
			Reference connectedRef = graph.get(connectedRefId);
			// TODO maybe somewhere else...
			if (connectedRef.getLabel() == null) {
				LOG.debug("setting label to {}", connectedRef);
				connectedRef.setLabel(label);
				
				refQueue.add(connectedRef);
			}
		}
		
	}
	
}
