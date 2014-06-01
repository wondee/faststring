package de.unifrankfurt.faststring.analysis.graph;

import static de.unifrankfurt.faststring.analysis.util.IRUtil.METHOD_SUBSTRING;
import static de.unifrankfurt.faststring.analysis.util.IRUtil.METHOD_SUBSTRING_DEFAULT_START;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;

import de.unifrankfurt.faststring.analysis.BaseAnalysisTest;
import de.unifrankfurt.faststring.analysis.StringCallIdentifier;

public class TestDataFlowGraph extends BaseAnalysisTest {

	
	@Test
	public void testPhiLoop() {
		
		DataFlowGraph graph = createGraph("MethodAnalyzerTestClass", "phiLoop");
		
		assertTrue(graph.contains(4));
		assertTrue(graph.contains(5));
		assertTrue(graph.contains(7));
		assertTrue(graph.contains(10));
		assertTrue(graph.contains(11));
		
//		assertTrue(graph.hasEdge(5, 11));
//		assertTrue(graph.hasEdge(4, 7));
//		assertTrue(graph.hasEdge(11, 7));
		
		assertEquals(5, graph.size());
	}

	@Test
	public void testSimpleIfSubstring() {
		DataFlowGraph graph = createGraph("MethodAnalyzerTestClass", "simpleIfSubstring");

		
		assertTrue(graph.contains(4));
		assertTrue(graph.contains(5));
		assertTrue(graph.contains(9));
		assertTrue(graph.contains(12));
		assertTrue(graph.contains(16));
		assertTrue(graph.contains(22));
		assertTrue(graph.contains(20));
		assertTrue(graph.contains(23));
		
//		assertTrue(graph.hasEdge(22, 23));
//		assertTrue(graph.hasEdge(20, 23));		
		
		assertEquals(8, graph.size());
	}

	
	@Test
	public void testPhiLoopAndIf() {
		DataFlowGraph graph = createGraph("MethodAnalyzerTestClass", "phiLoopAndIf");

		assertTrue(graph.contains(4));
		assertTrue(graph.contains(5));
		assertTrue(graph.contains(7));
		assertTrue(graph.contains(11));
		assertTrue(graph.contains(12));
		assertTrue(graph.contains(16));
		assertTrue(graph.contains(8));
		assertTrue(graph.contains(15));
		assertTrue(graph.contains(19));
		
//		assertTrue(graph.hasEdge(12, 12));
//		assertTrue(graph.hasEdge(11, 12));
//		assertTrue(graph.hasEdge(5, 16));
//		assertTrue(graph.hasEdge(15, 16));
//		assertTrue(graph.hasEdge(4, 7));
//		assertTrue(graph.hasEdge(16, 8));
		
		assertEquals(9, graph.size());
	}

	@Test
	public void testParamDef() {
		DataFlowGraph graph = createGraph("MethodAnalyzerTestClass", "paramDef");

		assertTrue(graph.contains(2));
		assertTrue(graph.contains(6));
		assertTrue(graph.contains(10));
		
		assertEquals(3, graph.size());
	}
	
	@Test
	@Ignore
	public void testFindPredeseccors() {
		DataFlowGraph graph = createGraph("MethodAnalyzerTestClass", "phiLoopAndIf");

//		assertList(graph.findAllPredeseccors(8), 12, 16, 4, 11, 5, 15);
//		assertList(graph.findAllPredeseccors(7), 4, 5);
//		assertListEmpty(graph.findAllPredeseccors(5));
//		assertList(graph.findAllPredeseccors(12), 4, 11, 12);
//		
	}

	@Test
	@Ignore
	public void testFindSuccessors() {
		DataFlowGraph graph = createGraph("MethodAnalyzerTestClass", "phiLoopAndIf");

//		assertList(graph.findAllSuccessors(4), 7, 8, 12);
//		assertListEmpty(graph.findAllSuccessors(7));
//		assertListEmpty(graph.findAllSuccessors(8));
//		assertList(graph.findAllSuccessors(5), 7, 8, 16);
		
	}
	
	private DataFlowGraph createGraph(String className, String methodName) {
		IR ir = getIR(className, methodName);
		DefUse defUse = new DefUse(ir);
		
		
		return new DataFlowGraphBuilder(new  StringCallIdentifier(METHOD_SUBSTRING, METHOD_SUBSTRING_DEFAULT_START), ir, defUse).createDataFlowGraph();
	}
	
}
