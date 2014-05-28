package de.unifrankfurt.faststring.analysis.graph;

import static de.unifrankfurt.faststring.analysis.util.TestUtilities.assertList;
import static de.unifrankfurt.faststring.analysis.util.TestUtilities.assertListEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Queues;
import com.ibm.wala.ssa.DefUse;

import de.unifrankfurt.faststring.analysis.BaseAnalysisTest;
import de.unifrankfurt.faststring.analysis.model.StringReference;
import de.unifrankfurt.faststring.analysis.model.StringUse;

public class TestIntreproceduralPointerGraph extends BaseAnalysisTest {

	@Before
	public void setUp() {
	}
	
	@Test
	public void testPhiLoop() {
		
		IntraproceduralPointerGraph graph = createGraph("MethodAnalyzerTestClass", "phiLoop", new StringUse(7, 14));
		
		assertTrue(graph.contains(4));
		assertTrue(graph.contains(5));
		assertTrue(graph.contains(7));
		assertTrue(graph.contains(10));
		assertTrue(graph.contains(11));
		
		assertTrue(graph.hasEdge(5, 11));
		assertTrue(graph.hasEdge(4, 7));
		assertTrue(graph.hasEdge(11, 7));
		
		assertEquals(5, graph.size());
	}

	@Test
	public void testSimpleIfSubstring() {
		IntraproceduralPointerGraph graph = createGraph("MethodAnalyzerTestClass", "simpleIfSubstring", new StringUse(22, 9));
		

		assertTrue(graph.contains(22));
		assertTrue(graph.contains(20));
		assertTrue(graph.contains(23));
		
		assertTrue(graph.hasEdge(22, 23));
		assertTrue(graph.hasEdge(20, 23));		
		
		assertEquals(3, graph.size());
	}

	
	@Test
	public void testPhiLoopAndIf() {
		IntraproceduralPointerGraph graph = createGraph("MethodAnalyzerTestClass", "phiLoopAndIf", 
				new StringUse(7, 36), new StringUse(12, 29));

		assertTrue(graph.contains(4));
		assertTrue(graph.contains(5));
		assertTrue(graph.contains(7));
		assertTrue(graph.contains(11));
		assertTrue(graph.contains(12));
		assertTrue(graph.contains(16));
		assertTrue(graph.contains(8));
		assertTrue(graph.contains(15));
		
		assertTrue(graph.hasEdge(12, 12));
		assertTrue(graph.hasEdge(11, 12));
		assertTrue(graph.hasEdge(5, 16));
		assertTrue(graph.hasEdge(15, 16));
		assertTrue(graph.hasEdge(4, 7));
		assertTrue(graph.hasEdge(16, 8));
		
		assertEquals(8, graph.size());
	}

	@Test
	public void testFindPredeseccors() {
		IntraproceduralPointerGraph graph = createGraph("MethodAnalyzerTestClass", "phiLoopAndIf", 
				new StringUse(7, 36), new StringUse(12, 29));

		assertList(graph.findAllPredeseccors(8), 12, 16, 4, 11, 5, 15);
		assertList(graph.findAllPredeseccors(7), 4, 5);
		assertListEmpty(graph.findAllPredeseccors(5));
		assertList(graph.findAllPredeseccors(12), 4, 11, 12);
		
	}
	
	private IntraproceduralPointerGraph createGraph(String className, String methodName, StringReference...references ) {
		DefUse defUse = new DefUse(getIR(className, methodName));
		
		Queue<StringReference> stringUses = Queues.newArrayDeque(Arrays.asList(references));
		
		IntraproceduralPointerGraph graph = GraphBuilder.create(defUse, stringUses);
		return graph;
	}
	
}
