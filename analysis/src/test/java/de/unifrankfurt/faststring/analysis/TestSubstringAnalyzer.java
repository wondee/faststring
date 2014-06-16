package de.unifrankfurt.faststring.analysis;

import static de.unifrankfurt.faststring.analysis.util.TestUtilities.assertList;

import java.util.Set;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.graph.GraphUtil;
import de.unifrankfurt.faststring.analysis.graph.Reference;

public class TestSubstringAnalyzer extends BaseAnalysisTest {

	private static final String TEST_CLASS = "MethodAnalyzerTestClass";
	
	
	@Test 
	public void testSimpleIfSubstring() {
		Set<Reference> candidates = findCandidates("simpleIfSubstring");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 5, 9, 16, 22, 23);
		assertList(GraphUtil.extractDefConversions(candidates), 4, 5);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 16, 23);
	}

	@Test  
	public void testReturnOfUsed() {
		Set<Reference> candidates = findCandidates("returnOfUsed");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 7);
		assertList(GraphUtil.extractDefConversions(candidates), 4);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 4, 7);
		
	}
	
	@Test
	public void testPhiSimple() {
		Set<Reference> candidates = findCandidates("phiSimple");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 7, 10);
		assertList(GraphUtil.extractDefConversions(candidates), 4);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 7);
	}

	@Test 
	public void testPhiLoop() {
		Set<Reference> candidates = findCandidates("phiLoop");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 5, 7, 10, 11);
		assertList(GraphUtil.extractDefConversions(candidates), 4, 5);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 11);
	}
	
	@Test
	public void testPhiLoopAndIf() {
		Set<Reference> candidates = findCandidates("phiLoopAndIf");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 5, 7, 8, 11, 12, 15, 16, 19);
		assertList(GraphUtil.extractDefConversions(candidates), 4, 5);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 16);
	}

	@Test
	public void testUsedAsCtorParam() {
		Set<Reference> candidates = findCandidates("usedAsCtorParam");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 2, 6);
		assertList(GraphUtil.extractDefConversions(candidates), 2);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 6);
	}
	
	private Set<Reference> findCandidates(String method) {
		return new SubstringAnalyzer(getTargetApplication(), getIR(TEST_CLASS, method).getMethod()).findCandidates();
	}
	
}
