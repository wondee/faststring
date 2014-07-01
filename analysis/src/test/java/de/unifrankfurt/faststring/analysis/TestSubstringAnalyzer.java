package de.unifrankfurt.faststring.analysis;

import static de.unifrankfurt.faststring.analysis.utils.TestUtilities.assertList;

import java.util.Collection;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraphBuilder;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.BuiltInTypes;
import de.unifrankfurt.faststring.analysis.util.GraphUtil;
import de.unifrankfurt.faststring.analysis.utils.BaseAnalysisTest;

public class TestSubstringAnalyzer extends BaseAnalysisTest {

	private static final String TEST_CLASS = "MethodAnalyzerTestClass";


	@Test
	public void testSimpleIfSubstring() {
		Collection<Reference> candidates = getAnalysisResult("simpleIfSubstring");

		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 5, 9, 16, 22, 23);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 4, 5);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 9, 16, 23);

	}

	@Test
	public void testReturnOfUsed() {
		Collection<Reference> candidates = getAnalysisResult("returnOfUsed");

		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 7);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 4);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 4, 7);

	}

	@Test
	public void testPhiSimple() {
		Collection<Reference> candidates  = getAnalysisResult("phiSimple");

		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 7, 10);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 4);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 7);

	}

	@Test
	public void testPhiLoop() {
		Collection<Reference> candidates = getAnalysisResult("phiLoop");

		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 5, 7, 10, 11);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 4, 5);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 11);

	}

	@Test
	public void testPhiLoopAndIf() {
		Collection<Reference> candidates = getAnalysisResult("phiLoopAndIf");

		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 17, 16, 18, 21, 4, 5, 7, 8, 11, 14, 15);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 4, 5);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 18);

	}

	@Test
	public void testUsedAsCtorParam() {
		Collection<Reference> candidates  = getAnalysisResult("usedAsCtorParam");

		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 2, 6);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 2);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 6);

	}

	@Test
	public void testParamDef() {
		Collection<Reference> candidates  = getAnalysisResult("paramDef");

		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 2, 6, 10);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 2);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates));

	}

	@Test
	public void testEffectedVars() {
		Collection<Reference> candidates  = getAnalysisResult("effectedVars");

		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 3, 10, 11);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 3);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 11);

	}

	private Collection<Reference> getAnalysisResult(String method) {
		
		DataFlowGraph graph = new DataFlowGraphBuilder(BuiltInTypes.SUBSTRING, getIRMethod(TEST_CLASS, method)).createDataFlowGraph();
		LabelAnalyzer.analyzeLabel(graph);
		
		return graph.getAllLabelMatchingReferences();
	}

}
