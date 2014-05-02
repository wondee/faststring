package de.unifrankfurt.faststrings.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.unifrankfurt.faststring.core.StringListBuilder;
import de.unifrankfurt.faststring.core.SubstringString;

public class StringListBuilderTest {

	StringListBuilder __testee;
	StringBuilder original;

	private static final String APPENDIX = "test";
	private static final SubstringString APPENDIX_ = new SubstringString(APPENDIX);

	private static final String APPENDIX2 = "abcde";
	private static final SubstringString APPENDIX2_ = new SubstringString(APPENDIX2);

	@Test
	public void testSimple() {

		String exp = new StringBuilder().append(APPENDIX).toString();
		String act = new StringListBuilder().append(APPENDIX_).toString();

		assertEquals(exp, act);
	}

	@Test
	public void testWithMoreAppendices() {

		String exp = new StringBuilder().append(APPENDIX).append(APPENDIX2).toString();
		String act = new StringListBuilder().append(APPENDIX_).append(APPENDIX2_).toString();

		assertEquals(exp, act);
	}



}
