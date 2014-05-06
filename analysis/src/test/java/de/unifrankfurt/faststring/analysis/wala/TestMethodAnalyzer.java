package de.unifrankfurt.faststring.analysis.wala;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class TestMethodAnalyzer extends BaseAnalysisTest {

	private static final String TEST_CLASS = "MethodAnalyzerTestClass";
	
	private MethodAnalyzer __testee = new MethodAnalyzer();
	
	@Test
	public void testSimpleIfSubstring() {
		assertList(Arrays.asList(4), findCandidates("simpleIfSubstring"));
	}
	
	@Test
	public void testReturnOfUsed() {
		assertListEmpty(findCandidates("returnOfUsed"));
	}
	
	@Test
	public void testDoIf() {
		assertList(Arrays.asList(7), findCandidates("doIf"));
	}
	
	@Test
	public void testPhi1() {
		assertList(Arrays.asList(5), findCandidates("phi1"));
	}
	
	@Test
	public void testPhi2() {
		assertList(Arrays.asList(4, 5), findCandidates("phi2"));
	}
	
	private void assertListEmpty(Set<Integer> candidates) {
		assertTrue(candidates.isEmpty());
		
	}

	private void assertList(List<Integer> expected, Set<Integer> candidates) {
		assertTrue(candidates.containsAll(expected));
		assertEquals(candidates.size(), expected.size());
	}
	
	
	private Set<Integer> findCandidates(String method) {
		return __testee.findCandidates(getIR(TEST_CLASS, method).getMethod());
	}
	
}
