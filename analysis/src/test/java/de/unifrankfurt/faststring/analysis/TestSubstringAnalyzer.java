package de.unifrankfurt.faststring.analysis;

import static de.unifrankfurt.faststring.analysis.util.TestUtilities.assertList;

import java.util.Set;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.graph.GraphUtil;
import de.unifrankfurt.faststring.analysis.graph.StringReference;

public class TestSubstringAnalyzer extends BaseAnalysisTest {

	private static final String TEST_CLASS = "MethodAnalyzerTestClass";
	
	
	@Test 
	public void testSimpleIfSubstring() {
		Set<StringReference> candidates = findCandidates("simpleIfSubstring");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 5, 9, 12, 16, 22, 23);
		assertList(GraphUtil.extractDefConversions(candidates), 4, 5);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 12, 16, 23);
	}

	@Test  
	public void testReturnOfUsed() {
		Set<StringReference> candidates = findCandidates("returnOfUsed");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 7);
		assertList(GraphUtil.extractDefConversions(candidates), 4);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 4, 7);
		
	}
	
	@Test
	public void testPhiSimple() {
		Set<StringReference> candidates = findCandidates("phiSimple");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 7, 10);
		assertList(GraphUtil.extractDefConversions(candidates), 4);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 7);
	}

	@Test 
	public void testPhiLoop() {
		Set<StringReference> candidates = findCandidates("phiLoop");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 5, 7, 10, 11);
		assertList(GraphUtil.extractDefConversions(candidates), 4, 5);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 11);
	}
	
	@Test
	public void testPhiLoopAndIf() {
		Set<StringReference> candidates = findCandidates("phiLoopAndIf");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 5, 7, 8, 11, 12, 15, 16, 19);
		assertList(GraphUtil.extractDefConversions(candidates), 4, 5);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 16);
	}

	private Set<StringReference> findCandidates(String method) {
		return new SubstringAnalyzer(getTargetApplication(), getIR(TEST_CLASS, method).getMethod()).findCandidates();
	}
	
}
