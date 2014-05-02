package de.unifrankfurt.faststrings.core;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unifrankfurt.faststring.core.StringTreeBuilder;
import de.unifrankfurt.faststring.core.SubstringString;

public class StringTreeBuilderTest {
	
	StringTreeBuilder __testee;
	StringBuilder original;
	
	private static final String APPENDIX = "test";
	private static final SubstringString APPENDIX_ = new SubstringString(APPENDIX);
	
	private static final String APPENDIX2 = "abcde";
	private static final SubstringString APPENDIX2_ = new SubstringString(APPENDIX2);
	
	
	@Test
	public void testSimple() {
		String act = new StringTreeBuilder(APPENDIX_).toString();
		String exp = new StringBuilder(APPENDIX).toString();
		
		assertEquals(exp, act);
	}
	
	public void testSimpleWithTwo() {
		String act = new StringTreeBuilder(APPENDIX_).append(APPENDIX2_).toString();
		String exp = new StringBuilder(APPENDIX).append(APPENDIX2).toString();
		
		assertEquals(exp, act);
	}
	
	
}
