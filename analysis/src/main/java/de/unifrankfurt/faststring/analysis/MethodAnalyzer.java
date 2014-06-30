package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.model.Use;

public class MethodAnalyzer {

	private static final Logger LOG = LoggerFactory
			.getLogger(MethodAnalyzer.class);

	private IRMethod method;
	private LabelAnalyzer analyzer;

	public MethodAnalyzer(IRMethod m, LabelAnalyzer analyzer) {
		this.method = m;
		this.analyzer = analyzer;
	}

	public AnalysisResult analyze() {
		Collection<Reference> refs = analyzer.analyzeLabel(method);

		calculateMissingLocals(refs);

		AnalysisResult analysisResult = new AnalysisResult(refs, method.getMaxLocals(), analyzer.getLabel());

		return analysisResult;
	}

	private void calculateMissingLocals(Collection<Reference> refs) {


		for (Reference ref : refs) {

//			Set<Integer> phiPointer = method.findAllUsesPhiPointer(ref.getRef());

			LOG.trace("checking for {} ", ref);

			Set<Integer> newLocals = Sets.newHashSet(ref.getDef().getLocalVariableIndex());

			for (Use  use : ref.getUses()) {



				Collection<Integer> locals = use.getLocalVariableIndex();
				LOG.trace("use: {}", locals);

				for (Integer local : locals) {
					if (!newLocals.contains(local)) {
						System.out.println("neuen gefunden! " + local);
						newLocals.add(local);
					}
				};
			}

			ref.getDef().setLocalVariableIndices(newLocals);
		}

	}

}
