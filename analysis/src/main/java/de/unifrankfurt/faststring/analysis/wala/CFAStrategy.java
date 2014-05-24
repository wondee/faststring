package de.unifrankfurt.faststring.analysis.wala;

import static com.ibm.wala.ipa.callgraph.impl.Util.*;

import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.propagation.SSAPropagationCallGraphBuilder;
import com.ibm.wala.ipa.cha.IClassHierarchy;

public enum CFAStrategy {
	ZERO {
		@Override
		public SSAPropagationCallGraphBuilder createBuilder(
				AnalysisOptions options, AnalysisCache cache,
				IClassHierarchy cha, AnalysisScope scope) {
			return makeZeroCFABuilder(options, cache, cha, scope);
		}
		
	},
	ZERO_ONE {
		@Override
		public SSAPropagationCallGraphBuilder createBuilder(
				AnalysisOptions options, AnalysisCache cache,
				IClassHierarchy cha, AnalysisScope scope) {
			return makeZeroOneCFABuilder(options, cache, cha, scope);
		}
	} , 
	ZERO_ONE_CONTAINER {

		@Override
		public SSAPropagationCallGraphBuilder createBuilder(
				AnalysisOptions options, AnalysisCache cache,
				IClassHierarchy cha, AnalysisScope scope) {
			return makeZeroOneContainerCFABuilder(options, cache, cha, scope);
		}
		
	},
	VANILLA_ZERO_ONE {
		@Override
		public SSAPropagationCallGraphBuilder createBuilder(
				AnalysisOptions options, AnalysisCache cache,
				IClassHierarchy cha, AnalysisScope scope) {
			return makeVanillaZeroOneCFABuilder(options, cache, cha, scope);
		}
	},
	/**
	 * @deprecated should not be used due to OutOfMemoryError
	 */
	@Deprecated
	VANILLA_ZERO_ONE_CONTAINER {
		@Override
		public SSAPropagationCallGraphBuilder createBuilder(
				AnalysisOptions options, AnalysisCache cache,
				IClassHierarchy cha, AnalysisScope scope) {
			return makeVanillaZeroOneContainerCFABuilder(options, cache, cha, scope);
		}
	};
	public abstract SSAPropagationCallGraphBuilder createBuilder(AnalysisOptions options, AnalysisCache cache,
		      IClassHierarchy cha, AnalysisScope scope);
}
