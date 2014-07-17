package de.unifrankfurt.faststring.analysis.util;

import static de.unifrankfurt.faststring.utils.TestUtilities.assertList;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;

import de.unifrankfurt.faststring.utils.BaseAnalysisTest;

public class TestIRUtil extends BaseAnalysisTest {


	@Test
	public void testPhiLoop() {
		assertList(getAllPointersFor("phiLoop", 8), 4, 5, 8, 11, 12);
		assertList(getAllPointersFor("phiLoop", 12), 5, 11, 12);
		assertList(getAllPointersFor("phiLoop", 5), 5);
		assertList(getAllPointersFor("phiLoop", 4), 4);

	}

	@Test
	public void testParamDef() {
		assertList(getAllPointersFor("paramDef", 2), 2);
		assertList(getAllPointersFor("paramDef", 6), 6);

	}

	@Test
	@Ignore
	public void testphiLoopAndIf() {
		assertList(getAllPointersFor("phiLoopAndIf", 12), 4, 11, 12);
		assertList(getAllPointersFor("phiLoopAndIf", 7), 4, 5, 7);
		assertList(getAllPointersFor("phiLoopAndIf", 16), 5, 15, 16);

	}

	@Test
	public void testPhiSimple() throws Exception {
		assertList(getAllPointersFor("phiSimple", 4), 4);
		assertList(getAllPointersFor("phiSimple", 7), 4, 6, 7);

	}

	private Collection<Integer> getAllPointersFor(String method, int valueNumber) {
		return IRUtil.findAllDefPointersFor(getDefUse("MethodAnalyzerTestClass", method), valueNumber);
	}
}
