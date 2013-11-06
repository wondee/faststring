package de.unifrankfurt.faststrings.core;


import static org.junit.Assert.*;

import org.junit.Test;


public class ReplaceRegexTest {

	private String original = "abcabc";

	private ReplaceRegexString _testee = new ReplaceRegexString(original);

	@Test
	public void testUnused() {
		assertEquals(original, _testee.toString());
	}

	@Test
	public void testSimpleReplaceLonger() {
		String regex = "abc";
		String replacement = "Hallo";
		SubstringString replacementS = new SubstringString(replacement);

		String exp = original.replaceAll(regex, replacement);
		String act = _testee.replaceAll(regex, replacementS).toString();

		assertEquals(exp, act);
	}

	@Test
	public void testSimpleReplaceShorter() {
		String regex = "abc";
		String replacement = "aa";
		SubstringString replacementS = new SubstringString(replacement);

		String exp = original.replaceAll(regex, replacement);
		String act = _testee.replaceAll(regex, replacementS).toString();

		assertEquals(exp, act);
	}

	@Test
	public void testSimpleReplace() {
		String regex = "abc";
		String replacement = "cba";
		SubstringString replacementS = new SubstringString(replacement);

		String exp = original.replaceAll(regex, replacement);
		String act = _testee.replaceAll(regex, replacementS).toString();

		assertEquals(exp, act);
	}

}
