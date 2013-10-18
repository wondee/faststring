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
}
