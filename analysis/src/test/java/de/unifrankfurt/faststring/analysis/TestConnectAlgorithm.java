package de.unifrankfurt.faststring.analysis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.IRAnalyzer;
import de.unifrankfurt.faststring.analysis.IRUtil;

/**
 * test for {@link IRUtil#isConnected(com.ibm.wala.ssa.IR, int, com.ibm.wala.ssa.SSAInstruction)}
 * 
 * @author markus
 *
 */
public class TestConnectAlgorithm extends BaseAnalysisTest {

	
	private final static String TEST_CLASS = "ConnectTestClass";
	
	
	@Test
	public void simpleTest() {
		IRAnalyzer analyzer = new IRAnalyzer(getIR(TEST_CLASS, "simple"));
				
		assertTrue(analyzer.isConnected(4, 7));
		assertFalse(analyzer.isConnected(7, 4));
		assertFalse(analyzer.isConnected(4, 4));
			
	}
	
	@Test
	public void ifTest() {
		IRAnalyzer analyzer = new IRAnalyzer(getIR(TEST_CLASS, "ifTest"));
		
		assertFalse(analyzer.isConnected(14, 9));
		assertFalse(analyzer.isConnected(14, 14));
		assertFalse(analyzer.isConnected(20, 14));
		
		assertTrue(analyzer.isConnected(19, 25));
		assertTrue(analyzer.isConnected(9, 29));

	}
	
	@Test
	public void loopTest() {
		IRAnalyzer analyzer = new IRAnalyzer(getIR(TEST_CLASS, "loopTest"));
		
		assertTrue(analyzer.isConnected(17, 16));
		assertTrue(analyzer.isConnected(16, 16));
		assertTrue(analyzer.isConnected(8, 18));
		
		assertFalse(analyzer.isConnected(17, 4));
		assertFalse(analyzer.isConnected(18, 16));
		assertFalse(analyzer.isConnected(18, 8));
	}
	
	
	@Test
	public void phi1Test() {
		IRAnalyzer analyzer = new IRAnalyzer(getIR(TEST_CLASS, "phi1"));	
	}
	
}
