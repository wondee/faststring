package de.unifrankfurt.faststring.analysis.label;

import static de.unifrankfurt.faststring.analysis.utils.TestUtilities.assertList;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.BaseAnalysisTest;
import de.unifrankfurt.faststring.analysis.util.GraphUtil;

public class TestBaseTypeLabel extends BaseAnalysisTest {

	
	@Test
	public void testFindTypeUses() throws Exception {
		assertList(findTypeUses("phiLoop"), 7);
		assertList(findTypeUses("paramDef"), 2, 6);
		assertList(findTypeUses("simpleIfSubstring"), 4, 5);
		assertList(findTypeUses("phiLoopAndIf"), 7, 8, 12);
		assertList(findTypeUses("returnOfUsed"), 4);
		
	}
	
	private Iterable<Integer> findTypeUses(String method) {
		return GraphUtil.extractIntsFromStringReferences(
				new MockLabel().findTypeUses(getIRMethod("MethodAnalyzerTestClass", method)));

	}
}
