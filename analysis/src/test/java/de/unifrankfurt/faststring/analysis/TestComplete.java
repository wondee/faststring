package de.unifrankfurt.faststring.analysis;

import org.junit.Test;

public class TestComplete extends BaseAnalysisTest{

	
	@Test
	public void testParserTest() {
		
		
		SubstringAnalyzer analyzer = new SubstringAnalyzer(getTargetApplication(), getMethod("ByteCodeTestClass", "stringFromCallResult"));
		
		
		analyzer.findCandidates();
	}

}
