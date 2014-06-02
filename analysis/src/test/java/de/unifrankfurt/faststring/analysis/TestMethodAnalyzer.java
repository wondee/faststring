package de.unifrankfurt.faststring.analysis;

import static de.unifrankfurt.faststring.analysis.graph.GraphUtil.stringReferenceToInt;
import static de.unifrankfurt.faststring.analysis.util.TestUtilities.assertList;
import static de.unifrankfurt.faststring.analysis.util.TestUtilities.assertListEmpty;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Iterables;

@Ignore
public class TestMethodAnalyzer extends BaseAnalysisTest {

	private static final String TEST_CLASS = "MethodAnalyzerTestClass";
	
	private SubstringAnalyzer __testee;
	
	@Test
	public void testSimpleIfSubstring() {
		assertList(findCandidates("simpleIfSubstring"), 4);
	}
	
	@Test
	public void testReturnOfUsed() {
		assertListEmpty(findCandidates("returnOfUsed"));
	}
	
	
	@Test
	public void testPhiBefore() {
		assertListEmpty(findCandidates("phiBefore"));
	}
	
	@Test
	public void testPhiAfter() {
		assertList(findCandidates("phiAfter"), 4, 7);
	}

	@Test
	public void testPhiLoop() {
		assertListEmpty(findCandidates("phiLoop"));
	}
	
	@Test
	public void testPhiLoop2() {
		assertListEmpty(findCandidates("phiLoop2"));
	}
	
	@Test
	public void testPhiLoopAndIf() {
		assertListEmpty(findCandidates("phiLoopAndIf"));
	}
	
	
	private Iterable<Integer> findCandidates(String method) {
		__testee = new SubstringAnalyzer(getTargetApplication(), getIR(TEST_CLASS, method).getMethod());
		
		return Iterables.transform(__testee.findCandidates(), stringReferenceToInt);
	}
	
}
