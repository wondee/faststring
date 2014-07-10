package de.unifrankfurt.faststring.analysis.label;

import static de.unifrankfurt.faststring.utils.TestUtilities.assertList;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.util.GraphUtil;
import de.unifrankfurt.faststring.utils.BaseAnalysisTest;

public class TestBaseTypeLabel extends BaseAnalysisTest {


	@Test
	public void testFindTypeUses() throws Exception {
		assertList(findTypeUses("phiLoop"), 8);
		assertList(findTypeUses("paramDef"), 2, 6);
		assertList(findTypeUses("simpleIfSubstring"), 4, 5);
		assertList(findTypeUses("phiLoopAndIf"), 7, 10, 15);
		assertList(findTypeUses("returnOfUsed"), 4);

	}

	private Iterable<Integer> findTypeUses(String method) {
		return GraphUtil.extractIntsFromStringReferences(
				new MockLabel().findTypeUses(getIRMethod("MethodAnalyzerTestClass", method)));

	}
}
