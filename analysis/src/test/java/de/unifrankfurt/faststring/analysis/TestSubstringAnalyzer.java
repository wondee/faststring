package de.unifrankfurt.faststring.analysis;

import static de.unifrankfurt.faststring.analysis.util.TestUtilities.assertList;
import static de.unifrankfurt.faststring.analysis.util.TestUtilities.assertListEmpty;

import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import de.unifrankfurt.faststring.analysis.graph.GraphUtil;
import de.unifrankfurt.faststring.analysis.graph.StringReference;

public class TestSubstringAnalyzer extends BaseAnalysisTest {

	private static final String TEST_CLASS = "MethodAnalyzerTestClass";
	
	
	@Test 
	public void testSimpleIfSubstring() {
		Set<StringReference> candidates = findCandidates("simpleIfSubstring");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 5, 9, 12, 16, 22, 23);
		assertList(GraphUtil.extractDefConversions(candidates), 4);
	}

	@Test @Ignore 
	public void testReturnOfUsed() {
		Set<StringReference> candidates = findCandidates("returnOfUsed");
		
		assertListEmpty(GraphUtil.extractIntsFromStringReferences(candidates));
		
	}
	
	
	@Test
	public void testPhiBefore() {
		Set<StringReference> candidates = findCandidates("phiBefore");
		assertListEmpty(GraphUtil.extractIntsFromStringReferences(candidates));
	}
	
	@Test @Ignore
	public void testPhiAfter() {
		Set<StringReference> candidates = findCandidates("phiAfter");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 7);
	}

	@Test 
	public void testPhiLoop() {
		Set<StringReference> candidates = findCandidates("phiLoop");
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 7, 4, 11, 5, 10);
	}
	
	@Test @Ignore
	public void testPhiLoop2() {
		Set<StringReference> candidates = findCandidates("phiLoop2");
		assertListEmpty(GraphUtil.extractIntsFromStringReferences(candidates));
	}
	
	@Test @Ignore
	public void testPhiLoopAndIf() {
		Set<StringReference> candidates = findCandidates("phiLoopAndIf");
		assertListEmpty(GraphUtil.extractIntsFromStringReferences(candidates));
	}

	private Set<StringReference> findCandidates(String method) {
		return new SubstringAnalyzer(getTargetApplication(), getIR(TEST_CLASS, method).getMethod()).findCandidates();
	}
	
}
