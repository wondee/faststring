package de.unifrankfurt.faststrings.core;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unifrankfurt.faststring.core.ReplaceCharString;

public class ReplaceCharTest {


	private String original = "abcdcba";

	private ReplaceCharString _testee = new ReplaceCharString(original);

	@Test
	public void testUnused() {
		assertEquals(original, _testee.toString());
	}

	@Test
	public void testSimpleReplaceNothing() {
		String act = original.replace('x', 'm');
		String exp = _testee.replace('x', 'm').toString();

		assertEquals(exp, act);
	}

	@Test
	public void testReplaceOne() {
		String act = original.replace('d', 'm');
		String exp = _testee.replace('d', 'm').toString();

		assertEquals(exp, act);
	}

	@Test
	public void testReplaceTwo() {
		String act = original.replace('b', 'm');
		String exp = _testee.replace('b', 'm').toString();

		assertEquals(exp, act);
	}

	@Test
	public void testReplaceMultiple() {
		String act = original.replace('b', 'm').replace('a', 'm').replace('m', 'x');
		String exp = _testee.replace('b', 'm').replace('a', 'm').replace('m', 'x').toString();

		assertEquals(exp, act);
	}
}
