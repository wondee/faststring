package de.unifrankfurt.faststring.analysis.wala;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * test for {@link IRUtil#isConnected(com.ibm.wala.ssa.IR, int, com.ibm.wala.ssa.SSAInstruction)}
 * 
 * @author markus
 *
 */
public class TestConnectAlgorithm extends BaseAnalysisTest {

//	private static final Logger LOG = LoggerFactory.getLogger(TestConnectAlgorithm.class);
	
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
	
}
