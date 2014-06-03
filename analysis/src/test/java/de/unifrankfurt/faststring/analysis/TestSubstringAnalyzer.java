package de.unifrankfurt.faststring.analysis;

import static de.unifrankfurt.faststring.analysis.graph.GraphUtil.stringReferenceToInt;
import static de.unifrankfurt.faststring.analysis.util.TestUtilities.assertList;
import static de.unifrankfurt.faststring.analysis.util.TestUtilities.assertListEmpty;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Iterables;

public class TestSubstringAnalyzer extends BaseAnalysisTest {

	private static final String TEST_CLASS = "MethodAnalyzerTestClass";
	
	private SubstringAnalyzer __testee;
	
	@Test 
	public void testSimpleIfSubstring() {
		assertList(findCandidates("simpleIfSubstring"), 4, 5, 9, 12, 16, 22, 23);
	}
	
	@Test @Ignore 
	public void testReturnOfUsed() {
		assertListEmpty(findCandidates("returnOfUsed"));
	}
	
	
	@Test
	public void testPhiBefore() {
		assertListEmpty(findCandidates("phiBefore"));
	}
	
	@Test @Ignore
	public void testPhiAfter() {
		assertList(findCandidates("phiAfter"), 4, 7);
	}

	@Test 
	public void testPhiLoop() {
		assertList(findCandidates("phiLoop"), 7, 4, 11, 5, 10);
	}
	
	@Test @Ignore
	public void testPhiLoop2() {
		assertListEmpty(findCandidates("phiLoop2"));
	}
	
	@Test @Ignore
	public void testPhiLoopAndIf() {
		assertListEmpty(findCandidates("phiLoopAndIf"));
	}
	
	
	private Iterable<Integer> findCandidates(String method) {
		__testee = new SubstringAnalyzer(getTargetApplication(), getIR(TEST_CLASS, method).getMethod());
		
		return Iterables.transform(__testee.findCandidates(), stringReferenceToInt);
	}
	
}
