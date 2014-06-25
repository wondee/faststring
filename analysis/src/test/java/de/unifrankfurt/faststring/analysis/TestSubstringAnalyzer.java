package de.unifrankfurt.faststring.analysis;

import static de.unifrankfurt.faststring.analysis.utils.TestUtilities.assertList;

import java.util.Collection;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.util.GraphUtil;

public class TestSubstringAnalyzer extends BaseAnalysisTest {

	private static final String TEST_CLASS = "MethodAnalyzerTestClass";
	
	
	@Test 
	public void testSimpleIfSubstring() {
		AnalysisResult result = getAnalysisResult("simpleIfSubstring");
		Collection<Reference> candidates = result.getRefs();
		
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 5, 9, 16, 22, 23);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 4, 5);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 16, 23);
		
		assertList(result.getEffectedVars(), 2, 3, 4);
	}

	@Test  
	public void testReturnOfUsed() {
		AnalysisResult result = getAnalysisResult("returnOfUsed");
		Collection<Reference> candidates = result.getRefs();

		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 7);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 4);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 4, 7);
		
		assertList(result.getEffectedVars(), 2);
	}
	
	@Test
	public void testPhiSimple() {
		AnalysisResult result = getAnalysisResult("phiSimple");
		Collection<Reference> candidates = result.getRefs();
		
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 7, 10);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 4);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 7);
		
		assertList(result.getEffectedVars(), 2, 3);
	}

	@Test 
	public void testPhiLoop() {
		AnalysisResult result = getAnalysisResult("phiLoop");
		Collection<Reference> candidates = result.getRefs();
		
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 5, 7, 10, 11);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 4, 5);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 11);

		assertList(result.getEffectedVars(), 2, 3, 4);
	}
	
	@Test
	public void testPhiLoopAndIf() {
		AnalysisResult result = getAnalysisResult("phiLoopAndIf");
		Collection<Reference> candidates = result.getRefs();
		
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 4, 5, 7, 8, 11, 12, 15, 16, 19);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 4, 5);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 16);
		
		assertList(result.getEffectedVars(), 2, 3, 4, 5);
	}

	@Test
	public void testUsedAsCtorParam() {
		AnalysisResult result = getAnalysisResult("usedAsCtorParam");
		Collection<Reference> candidates = result.getRefs();
		
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 2, 6);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 2);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 6);
		
		assertList(result.getEffectedVars(), 1, 2);
	}
	
	@Test
	public void testParamDef() {
		AnalysisResult result = getAnalysisResult("paramDef");
		Collection<Reference> candidates = result.getRefs();
		
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 2, 6, 10);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 2);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates));
		
		assertList(result.getEffectedVars(), 1, 2);
	}
	
	@Test
	public void testEffectedVars() {
		AnalysisResult result = getAnalysisResult("effectedVars");
		Collection<Reference> candidates = result.getRefs();
		
		assertList(GraphUtil.extractIntsFromStringReferences(candidates), 3, 10, 11);
		assertList(GraphUtil.extractDefConversionsAsInt(candidates), 3);
		assertList(GraphUtil.extractUsageConversionsRefIds(candidates), 11);
		
		assertList(result.getEffectedVars(), 2, 4);
	}

	private AnalysisResult getAnalysisResult(String method) {
		return new SubstringAnalyzer(getTargetApplication(), getIR(TEST_CLASS, method).getMethod()).findCandidates();
	}
	
}
