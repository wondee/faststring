package de.unifrankfurt.faststring.analysis.label;

import static de.unifrankfurt.faststring.analysis.test.util.TestUtilities.assertList;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.util.GraphUtil;
import de.unifrankfurt.faststring.utils.BaseAnalysisTest;

public class TestBaseTypeLabel extends BaseAnalysisTest {


	@Test
	public void testFindTypeUses() throws Exception {
		assertList(findTypeUses("phiLoop"), 8);
		assertList(findTypeUses("paramDef"), 2, 6);
		assertList(findTypeUses("simpleIfSubstring"), 4, 5);
		assertList(findTypeUses("returnOfUsed"), 4);

	}

	@Test
	public void testFindTypeUsesByLabelFromFile() throws Exception {
		assertList(findTypeUsesByLabelFromFile("phiLoop"), 8);
		assertList(findTypeUsesByLabelFromFile("paramDef"), 2, 6);
		assertList(findTypeUsesByLabelFromFile("simpleIfSubstring"), 4, 5);
		assertList(findTypeUsesByLabelFromFile("returnOfUsed"), 4);

	}
	
	private Iterable<Integer> findTypeUses(String method) {
		return GraphUtil.extractIntsFromStringReferences(
				new MockLabel().findTypeUses(getIRMethod("MethodAnalyzerTestClass", method)));

	}
	
	private Iterable<Integer> findTypeUsesByLabelFromFile(String method) {
		return GraphUtil.extractIntsFromStringReferences(
				SUBSTRING.findTypeUses(getIRMethod("MethodAnalyzerTestClass", method)));

	}
	
}
