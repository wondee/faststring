package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wala.classLoader.IMethod;

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraphBuilder;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.BuiltInTypes;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.model.DataFlowObject;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;
import de.unifrankfurt.faststring.analysis.util.GraphUtil;
import de.unifrankfurt.faststring.analysis.util.QueueUtil;
import de.unifrankfurt.faststring.analysis.util.QueueUtil.BaseProcessingStrategy;
import de.unifrankfurt.faststring.analysis.util.QueueUtil.ProcessingStrategy;
import de.unifrankfurt.faststring.analysis.util.StringUtil;

public class SubstringAnalyzer {

	private static final Logger LOG = LoggerFactory.getLogger(SubstringAnalyzer.class);
		
	private IRMethod ir;

	private TypeLabel label = BuiltInTypes.SUBSTRING;

	private DataFlowGraph graph;

	
	public SubstringAnalyzer(TargetApplication targetApplication, IMethod m) {
		ir = targetApplication.findIRMethodForMethod(m);
		
	}
	
	
	public AnalysisResult findCandidates() {
		LOG.info("analyzing (for {}) Method: {}", label.getClass(), ir.getMethodSignature());
		
		graph = getGraph();
		Collection<Reference> refs = graph.getAllLabelMatchingReferences();
		
		if (refs.size() > 0) {
			QueueUtil.processUntilQueueIsEmpty(refs, definitionCheck);
			QueueUtil.processUntilQueueIsEmpty(graph.getAllLabelMatchingReferences(), usageCheck);
			
		} else {
			LOG.debug("no string uses found");
		}
		
		Collection<Reference> finalRefs = graph.getAllLabelMatchingReferences();
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("found possible references: {}", GraphUtil.extractIntsFromStringReferences(finalRefs));
			LOG.debug("definition conversion needed for: {}", StringUtil.toStringMap(GraphUtil.extractDefConversions(finalRefs)));
			LOG.debug("usage conversion needed for: {}", StringUtil.toStringMap(GraphUtil.extractUsageConversions(finalRefs)));
		}
		
		return new AnalysisResult(finalRefs);
		
	}


	private DataFlowGraph getGraph() {
		if (graph == null) {
			graph = new DataFlowGraphBuilder(label, ir).createDataFlowGraph();
		}
		return graph;
	}
	
	private ProcessingStrategy<Reference> definitionCheck = new BaseProcessingStrategy<Reference>() {
		@Override
		public void process(Reference ref, Queue<Reference> refQueue) {
			
			
			Definition def = ref.getDef();
			if (def.isCompatibleWith(label)) {
				check(def, refQueue);
				
			} else {
				ref.setConvertToDefinition();
			}
			
		}
	};
	
	private ProcessingStrategy<Reference> usageCheck = new BaseProcessingStrategy<Reference>() {
		@Override
		public void process(Reference ref, Queue<Reference> refQueue) {
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
		LOG.trace("inspecting {}", o);
		for (Integer connectedRefId : o.getConnectedRefs(label)) {
			Reference connectedRef = graph.get(connectedRefId);
			// TODO maybe somewhere else...
			if (connectedRef.getLabel() == null) {
				LOG.trace("setting label to {}", connectedRef);
				connectedRef.setLabel(label);
				
				refQueue.add(connectedRef);
			}
		}
		
	}
	
}
