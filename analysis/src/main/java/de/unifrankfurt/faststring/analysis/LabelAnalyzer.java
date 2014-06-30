package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraphBuilder;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.model.DataFlowObject;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;
import de.unifrankfurt.faststring.analysis.util.GraphUtil;
import de.unifrankfurt.faststring.analysis.util.QueueUtil;
import de.unifrankfurt.faststring.analysis.util.QueueUtil.BaseProcessingStrategy;
import de.unifrankfurt.faststring.analysis.util.StringUtil;

public class LabelAnalyzer {

	private static final Logger LOG = LoggerFactory.getLogger(LabelAnalyzer.class);

	private TypeLabel label;

	public LabelAnalyzer(TypeLabel label) {
		this.label = label;
	}


	public Collection<Reference> analyzeLabel(IRMethod ir) {
		LOG.info("analyzing (for {}) Method: {}", label.getClass(), ir.getMethodSignature());

		DataFlowGraph graph = new DataFlowGraphBuilder(label, ir).createDataFlowGraph();
		Collection<Reference> refs = graph.getAllLabelMatchingReferences();

		if (refs.size() > 0) {
			QueueUtil.processUntilQueueIsEmpty(refs, new DefinitionUsageCheckStrategy(graph));


		} else {
			LOG.debug("no uses found");
		}

		Collection<Reference> finalRefs = graph.getAllLabelMatchingReferences();

		if (LOG.isDebugEnabled() && finalRefs.size() > 0) {
			LOG.debug("found possible references: {}", GraphUtil.extractIntsFromStringReferences(finalRefs));
			LOG.debug("definition conversion needed for: {}", StringUtil.toStringMap(GraphUtil.extractDefConversions(finalRefs)));
			LOG.debug("usage conversion needed for: {}", StringUtil.toStringMap(GraphUtil.extractUsageConversions(finalRefs)));
		}

		return finalRefs;

	}

	private class DefinitionUsageCheckStrategy extends CheckStrategy {

		public DefinitionUsageCheckStrategy(DataFlowGraph graph) {
			super(graph);
		}

		@Override
		public void process(Reference ref, Queue<Reference> refQueue) {
			List<DataFlowObject> os = Lists.newLinkedList();

			os.add(ref.getDef());
			os.addAll(ref.getUses());

			for (DataFlowObject o : os) {
				if (o.isCompatibleWith(label)) {
					check(o, refQueue);

				} else {
					if (o instanceof Definition) {
						ref.setConvertToDefinition();
					} else {
						ref.setConvertToUse((Use)o);
					}

				}
			}


		}
	}

	private abstract class CheckStrategy extends BaseProcessingStrategy<Reference> {

		private DataFlowGraph graph;

		public CheckStrategy(DataFlowGraph graph) {
			this.graph = graph;
		}

		void check(DataFlowObject o, Queue<Reference> refQueue) {
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

//	private class DefinitionCheckStrategy extends CheckStrategy {
//
//		public DefinitionCheckStrategy(DataFlowGraph graph) {
//			super(graph);
//		}
//
//		@Override
//		public void process(Reference ref, Queue<Reference> refQueue) {
//
//
//			Definition def = ref.getDef();
//			if (def.isCompatibleWith(label)) {
//				check(def, refQueue);
//
//			} else {
//				ref.setConvertToDefinition();
//			}
//
//		}
//	}
//
//	private class UsageCheckStrategy extends CheckStrategy {
//		public UsageCheckStrategy(DataFlowGraph graph) {
//			super(graph);
//		}
//
//		@Override
//		public void process(Reference ref, Queue<Reference> refQueue) {
//			List<Use> uses = ref.getUses();
//			for (int useId = 0; useId < uses.size(); useId++) {
//				Use use = uses.get(useId);
//				if (use.isCompatibleWith(label)) {
//					check(use, refQueue);
//				} else {
//					ref.setConvertToUse(useId);
//
//				}
//			}
//
//		}
//	}

	public TypeLabel getLabel() {
		return label;
	};

}
