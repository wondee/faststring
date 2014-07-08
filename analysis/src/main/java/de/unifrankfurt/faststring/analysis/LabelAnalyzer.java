package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.PhiInstructionNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.util.GraphUtil;
import de.unifrankfurt.faststring.analysis.util.QueueUtil;
import de.unifrankfurt.faststring.analysis.util.QueueUtil.BaseQueueProcessingStrategy;
import de.unifrankfurt.faststring.analysis.util.StringUtil;

public class LabelAnalyzer extends BaseQueueProcessingStrategy<Reference>{

	private static final Logger LOG = LoggerFactory.getLogger(LabelAnalyzer.class);

	private final DataFlowGraph graph;

	private Stack<PhiInstructionNode> phis;

	private PhiLabelingStrategy strategy;

	public LabelAnalyzer(DataFlowGraph graph, PhiLabelingStrategy strategy) {
		this.graph = graph;
		phis = new Stack<>();
		this.strategy = strategy;
	}

	private Function<Integer, Reference> valueNumberToReference = new Function<Integer, Reference>() {
		@Override
		public Reference apply(Integer input) {
			return graph.get(input);
		}
	};

	public static void analyzeLabel(DataFlowGraph graph, PhiLabelingStrategy strategy) {
		analyzeLabel(graph, graph.getAllLabelMatchingReferences(), strategy);
	}

	public static void analyzeLabel(DataFlowGraph graph) {
		analyzeLabel(graph, graph.getAllLabelMatchingReferences(), new CountingStrategy());
	}

	public static void analyzeLabel(DataFlowGraph graph, Collection<Reference> refs, PhiLabelingStrategy strategy) {
		if (refs.size() > 0) {
			QueueUtil.processUntilQueueIsEmpty(refs, new LabelAnalyzer(graph, strategy));

			Collection<Reference> finalRefs = graph.getAllLabelMatchingReferences();

			if (LOG.isDebugEnabled() && finalRefs.size() > 0) {
				LOG.debug("found possible references: {}", GraphUtil.extractIntsFromStringReferences(finalRefs));
				LOG.debug("definition conversion to opt is needed for: {}", StringUtil.toStringMap(GraphUtil.extractDefConversionsToOpt(finalRefs)));
				LOG.debug("definition conversion from opt is needed for: {}", StringUtil.toStringMap(GraphUtil.extractDefConversionsFromOpt(finalRefs)));
//				LOG.debug("usage conversion needed for: {}", StringUtil.toStringMap(GraphUtil.extractUsageConversionsToOpt(finalRefs)));
			}

		} else {
			LOG.debug("no references to analyze");
		}
	}


	@Override
	public void process(Reference ref, Queue<Reference> refQueue) {

		List<InstructionNode> os = Lists.newLinkedList();

		os.add(ref.getDefinition());
		os.addAll(ref.getUses());

		for (InstructionNode o : os) {

			if (o instanceof PhiInstructionNode) {
				PhiInstructionNode phi = (PhiInstructionNode) o;

				if (!phis.contains(phi)) {

					phis.add(phi);
					Collection<Integer> connectedRefs = phi.getConnectedRefs(ref.getRef());

					connectedRefs.remove(ref.getRef());
					Collection<Reference> refs = Collections2.transform(connectedRefs,
							valueNumberToReference);

					refQueue.addAll(refs);
				}
			} else {
				final TypeLabel label = graph.getLabel();

				if (o.isCompatibleWith(label, ref.getRef())) {
					o.setLabel(label);
					LOG.trace("inspecting {}", o);
					for (Integer connectedRefId : o.getConnectedRefs(label, ref.getRef())) {
						Reference connectedRef = graph.get(connectedRefId);
						if (connectedRef.getLabel() == null) {
							LOG.trace("setting label to {}", connectedRef);
							connectedRef.setLabel(label);

							refQueue.add(connectedRef);
						}
					}

				}
			}
		}
	}

	@Override
	public void finished() {

		while(!phis.isEmpty()) {
			PhiInstructionNode phi = phis.pop();
			if (strategy.shouldBeLabeled(graph, phi)) {
				phi.setLabel(graph.getLabel());
			}
		}

		for (Reference ref : graph.getReferences()) {
			ref.createBarriers();

		}
	}


	public interface PhiLabelingStrategy {
		boolean shouldBeLabeled(DataFlowGraph graph, PhiInstructionNode phis);
	}



}
