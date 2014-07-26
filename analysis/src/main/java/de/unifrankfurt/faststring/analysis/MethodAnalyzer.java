package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraphBuilder;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.MethodCallNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class MethodAnalyzer {

	private static final Logger LOG = LoggerFactory.getLogger(MethodAnalyzer.class);

	private AnalyzedMethod method;

	private Collection<TypeLabel> labels;

	public MethodAnalyzer(AnalyzedMethod m, Collection<TypeLabel> labels) {
		this.method = m;
		this.labels = labels;
	}

	public MethodAnalyzer(AnalyzedMethod method, TypeLabel label) {
		this(method, Sets.newHashSet(label));
	}

	public AnalysisResult analyze() {
		LOG.info("analyzing {}", method.getSignature());

		Collection<Reference> refs = Sets.newHashSet();

		for (TypeLabel label : labels) {
			Collection<Reference> typeUses = label.findTypeUses(method);

			refs.addAll(typeUses);
		}

		LOG.debug("building graph");

		DataFlowGraph graph = new DataFlowGraphBuilder(method).createDataFlowGraph(refs, labels);

		if (!graph.isEmpty()) {
			LOG.debug("analyzing label");

			LabelAnalyzer.analyzeLabel(graph);

			Collection<Reference> finalRefs = graph.getAllLabelMatchingReferences();

			for (Reference ref : finalRefs) {
				List<InstructionNode> uses = ref.getUses();

				if (ref.getLabel() != null) {
					InstructionNode definition = ref.getDefinition();

					if (definition instanceof MethodCallNode) {
						((MethodCallNode)definition).setDefLabel(ref.getLabel());
					}

					for (InstructionNode use : uses) {
						if (use instanceof MethodCallNode) {
							((MethodCallNode)use).addLabelToUse(ref.valueNumber(), ref.getLabel());
						}
					}
				}
			}

			AnalysisResult analysisResult = new AnalysisResult(finalRefs, method.getMaxLocals(), method.getMethodName());

			return analysisResult;
		} else {
			LOG.trace("return empty result");
			return AnalysisResult.EMPTY;
		}

	}

}
