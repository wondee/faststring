package de.unifrankfurt.faststring.analysis.wala;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class TestMethodAnalyzer extends BaseAnalysisTest {

	private static final String TEST_CLASS = "MethodAnalyzerTestClass";
	
	private MethodAnalyzer __testee = new MethodAnalyzer();
	
	@Test
	public void testSimpleIfSubstring() {
		
		Set<Integer> candidates = findCandidates("simpleIfSubstring");
		
		assertEquals(1, candidates.size());
		assertTrue(candidates.contains(4));
		
	}
	
	@Test
	public void testReturnOfUsed() {
		Set<Integer> candidates = findCandidates("returnOfUsed");
		
		assertTrue(candidates.isEmpty());
		
	}
	
	
	private Set<Integer> findCandidates(String method) {
		return __testee.findCandidates(getIR(TEST_CLASS, method).getMethod());
	}
	
}
