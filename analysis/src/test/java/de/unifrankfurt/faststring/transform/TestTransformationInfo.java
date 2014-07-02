package de.unifrankfurt.faststring.transform;

import static de.unifrankfurt.faststring.analysis.utils.TestUtilities.assertList;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.AnalysisResult;
import de.unifrankfurt.faststring.analysis.MethodAnalyzer;
import de.unifrankfurt.faststring.analysis.label.BuiltInTypes;
import de.unifrankfurt.faststring.analysis.utils.BaseAnalysisTest;

public class TestTransformationInfo extends BaseAnalysisTest  {
	private static final String TEST_CLASS = "MethodAnalyzerTestClass";
	
	
	@Test 
	public void testSimpleIfSubstring() {
		TransformationInfo result = getTransformationInfo("simpleIfSubstring");
	
//		assertList(result.getEffectedVars(), 2, 3, 4);
	}

	@Test  
	public void testReturnOfUsed() {
		TransformationInfo result = getTransformationInfo("returnOfUsed");
		
//		assertList(result.getEffectedVars(), 2);
	}
	
	@Test
	public void testPhiSimple() {
		TransformationInfo result = getTransformationInfo("phiSimple");
		
//		assertList(result.getEffectedVars(), 2, 3);
	}

	@Test 
	public void testPhiLoop() {
		TransformationInfo result = getTransformationInfo("phiLoop");

//		assertList(result.getEffectedVars(), 2, 3, 4);
	}
	
	@Test
	public void testPhiLoopAndIf() {
		TransformationInfo result = getTransformationInfo("phiLoopAndIf");
		
//		assertList(result.getEffectedVars(), 2, 3, 4, 5);
	}

	@Test
	public void testUsedAsCtorParam() {
		TransformationInfo result = getTransformationInfo("usedAsCtorParam");
		
//		assertList(result.getEffectedVars(), 1, 2);
	}

	@Test
	public void testParamDef() {
		TransformationInfo result = getTransformationInfo("paramDef");
		
//		assertList(result.getEffectedVars(), 1, 2);
	}
	
	@Test
	public void testEffectedVars() {
		TransformationInfo result = getTransformationInfo("effectedVars");

//		assertList(result.getEffectedVars(), 2, 4);
	}
	
	private TransformationInfo getTransformationInfo(String methodName) {
		AnalysisResult result = new MethodAnalyzer(getIRMethod(TEST_CLASS, methodName), BuiltInTypes.SUBSTRING).analyze();
//		return new TransformationInfo(result);
		return null;
	}

}
