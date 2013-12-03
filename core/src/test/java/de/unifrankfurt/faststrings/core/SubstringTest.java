package de.unifrankfurt.faststrings.core;


import static org.junit.Assert.*;

import org.junit.Test;

public class SubstringTest {

	String original = "abcdefghijklmnopqrstuvwxyz";

	SubstringString _testee = new SubstringString(original);

	@Test
	public void testUnused() {
		assertEquals(original, _testee.toString());
	}

	@Test
	public void testSimpleSubstring() {
		int pos = 5;

		String exp = original.substring(pos);
		String act = _testee.substring(pos).toString();

		assertEquals(exp, act);
	}

	@Test
	public void testStartAndEnd() {
		int start = 7;
		int end = 11;

		String exp = original.substring(start, end);
		String act = _testee.substring(start, end).toString();

		assertEquals(exp, act);
	}

	@Test
	public void testMultipleSubstrings() {
		int start1 = 3;
		int end1 = 21;

		int start2 = 3;
		int end2 = 12;

		String exp = original.substring(start1, end1).substring(start2, end2);
		String act = _testee.substring(start1, end1).substring(start2, end2).toString();

		assertEquals(exp, act);

		exp = original.substring(end2, end1);
		act = _testee.substring(end2, end1).toString();

		assertEquals(exp, act);
	}
	
	@Test
	public void testLength() {
		int start = 3;
		int end = 12;
		
		int act = _testee.substring(start, end).length();
		int exp = original.substring(start, end).length();
		
		assertEquals(exp, act);
	}
	
	@Test
	public void testCharAt() {
		int start = 5;
		int end = 15;
		
		int index = 4;
		
		char act = _testee.substring(start, end).charAt(index);
		char exp = original.substring(start, end).charAt(index);
		assertEquals(exp, act);
	}
}
