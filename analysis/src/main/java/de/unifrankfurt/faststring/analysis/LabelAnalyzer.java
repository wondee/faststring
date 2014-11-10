package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.LabelableNode;
import de.unifrankfurt.faststring.analysis.graph.PhiNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.util.GraphUtil;
import de.unifrankfurt.faststring.analysis.util.QueueUtil;
import de.unifrankfurt.faststring.analysis.util.QueueUtil.BaseQueueProcessingStrategy;

/**
 * Holds the algorithm to build the bubble within a method.
 * <p>
 * Call the algorithm via the static methods
 * <ul>
 * <li>{@link #analyzeLabel(DataFlowGraph)}</li>
 * <li>{@link #analyzeLabel(DataFlowGraph, PhiLabelingStrategy)}</li>
 * <li>{@link #analyzeLabel(DataFlowGraph, Collection, PhiLabelingStrategy)}</li>
 * </ul>
 *
 * @author markus
 *
 */
public class LabelAnalyzer extends BaseQueueProcessingStrategy<Reference>{

	private static final Logger LOG = LoggerFactory.getLogger(LabelAnalyzer.class);

	private final DataFlowGraph graph;

	private Stack<PhiNode> phis;

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
			}

		} else {
			LOG.debug("no references to analyze");
		}
	}


	@Override
	public void process(Reference ref, Queue<Reference> refQueue) {
		LOG.trace("queue: {}, ref: {}", GraphUtil.extractIntsFromStringReferences(refQueue), ref);

		InstructionNode defNode = ref.getDefinition();
		TypeLabel label = ref.getLabel();

		if (ref.getLabel() != null) {

			if (defNode instanceof PhiNode) {
				processPhi(ref, refQueue,(PhiNode) defNode);
			} else {
				if (defNode instanceof LabelableNode) {
					LabelableNode labelable = (LabelableNode) defNode;

					if (labelable.canProduce(label)) {
						labelConnectedRefs(label, refQueue, labelable);
					}
				}
			}

			for (InstructionNode useNode : ref.getUses()) {
				if (useNode instanceof PhiNode) {
					processPhi(ref, refQueue,(PhiNode) useNode);
				} else {
					if (useNode instanceof LabelableNode) {
						LabelableNode labelable = (LabelableNode) useNode;
						for (int i : labelable.getIndicesForV(ref.valueNumber())) {
							if (labelable.canUseAt(label, i)) {
								labelConnectedRefs(label, refQueue, labelable);
							}
						}
					}
				}
			}
		}
	}

	private void labelConnectedRefs(TypeLabel label , Queue<Reference> refQueue, LabelableNode node) {
		node.setLabel(label);
		List<Integer> connectedRefs = node.getLabelableRefs(label);

		for (int v : connectedRefs) {
			Reference ref = graph.get(v);

			if (ref.getLabel() == null) {
				ref.setLabel(label);
				refQueue.add(ref);
			}
		}
	}

	private void processPhi(Reference ref, Queue<Reference> refQueue, PhiNode o) {
		PhiNode phi = (PhiNode) o;

		if (!phis.contains(phi)) {

			phis.add(phi);
			Collection<Integer> connectedRefs = phi.getConnectedRefs(ref.valueNumber());

			Collection<Reference> refs = Collections2.transform(connectedRefs,
					valueNumberToReference);

			refQueue.addAll(refs);
		}
	}

	@Override
	public void finished() {
		while(!phis.isEmpty()) {
			PhiNode phi = phis.pop();
			phi.setLabel(strategy.shouldBeLabeled(graph, phi));
		}
	}


	public interface PhiLabelingStrategy {
		TypeLabel shouldBeLabeled(DataFlowGraph graph, PhiNode phis);
	}

}
