package de.unifrankfurt.faststring.analysis.wala;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class TestMethodAnalyzer extends BaseAnalysisTest {

	private static final String TEST_CLASS = "MethodAnalyzerTestClass";
	
	private MethodAnalyzer __testee;
	
	@Test
	public void testSimpleIfSubstring() {
		assertList(Arrays.asList(4), findCandidates("simpleIfSubstring"));
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
		assertList(Arrays.asList(4, 7), findCandidates("phiAfter"));
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
	
	private void assertListEmpty(Set<Integer> candidates) {
		assertTrue(candidates.isEmpty());
		
	}

	private void assertList(List<Integer> expected, Set<Integer> candidates) {
		assertTrue(candidates.containsAll(expected));
		assertEquals(candidates.size(), expected.size());
	}
	
	
	private Set<Integer> findCandidates(String method) {
		__testee = new MethodAnalyzer(getTargetApplication(), getIR(TEST_CLASS, method).getMethod());
		
		return __testee.findCandidates();
	}
	
}
