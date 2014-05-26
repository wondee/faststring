package de.unifrankfurt.faststring.analysis;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.unifrankfurt.faststring.analysis.UseRegister;

public class TestUseRegister {

	private static final List<Integer> ALL_NUMBERS = Arrays.asList(1,2,3,4);
	private static final List<Integer> FIRST_HALF = Arrays.asList(1,2);
	
	private UseRegister _testee;
	
	
	
	@Before
	public void setUp() {
		_testee = new UseRegister();
	}
	
	@Test
	public void testAllFalse() {
		assertTrue(_testee.add(1, false));
		assertTrue(_testee.add(2, false));
		assertTrue(_testee.add(3, false));
		assertTrue(_testee.add(4, false));
		
		Set<Integer> candidates = _testee.getCandidates();
		assertTrue(candidates.containsAll(ALL_NUMBERS));
		assertEquals(4, candidates.size());
		
	}
	
	@Test
	public void testAllTrue() {
		assertTrue(_testee.add(1, true));
		assertTrue(_testee.add(2, true));
		assertTrue(_testee.add(3, true));
		assertTrue(_testee.add(4, true));
		
		assertFalse(_testee.add(3, false));
		
		assertTrue(_testee.getCandidates().isEmpty());
	}
	
	@Test
	public void testOverride() {
		assertTrue(_testee.add(1, false));
		assertTrue(_testee.add(2, false));
		assertTrue(_testee.add(3, false));
		assertTrue(_testee.add(4, true));
		
		assertTrue(_testee.add(3, true));
		
		Set<Integer> candidates = _testee.getCandidates();
		assertTrue(candidates.containsAll(FIRST_HALF));
		assertEquals(2, candidates.size());
	}
	
	
}
