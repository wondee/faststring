package de.unifrankfurt.faststring.transform;

import static de.unifrankfurt.faststring.utils.TestUtilities.assertList;
import static org.hamcrest.collection.IsIn.isIn;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.AnalysisResult;
import de.unifrankfurt.faststring.analysis.MethodAnalyzer;
import de.unifrankfurt.faststring.core.label.BuiltInTypes;
import de.unifrankfurt.faststring.utils.BaseAnalysisTest;
 
public class TestTransformationInfo extends BaseAnalysisTest  {
	private static final String TEST_CLASS = "TransformationDefinitionTestClass";
	
	@Test
	public void testConstantDef() {
		TransformationInfo result = getTransformationInfo("phiDef");

		assertList(result.getEffectedVars(), 4);
		
//		assertThat(result.getLocalForLabel(null, BuiltInTypes.SUBSTRING, 2), not(isIn(result.getEffectedVars())));
//		assertThat(result.getLocalForLabel(null, BuiltInTypes.SUBSTRING, 3), not(isIn(result.getEffectedVars())));
		assertThat(result.getLocalForLabel(null, BuiltInTypes.SUBSTRING, 4), not(isIn(result.getEffectedVars())));
	
	}
	
	private TransformationInfo getTransformationInfo(String methodName) {
		AnalysisResult result = new MethodAnalyzer(getIRMethod(TEST_CLASS, methodName), BuiltInTypes.SUBSTRING).analyze();
		return new TransformationInfo(result);
	}

}
