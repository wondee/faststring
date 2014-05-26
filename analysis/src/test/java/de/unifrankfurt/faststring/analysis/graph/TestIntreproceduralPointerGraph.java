package de.unifrankfurt.faststring.analysis.graph;

import static org.junit.Assert.*;

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
		
		assertEquals(5, graph.size());
	}

	@Test
	public void testSimpleIfSubstring() {
		IntraproceduralPointerGraph graph = createGraph("MethodAnalyzerTestClass", "simpleIfSubstring", new StringUse(22, 9));
		

		assertTrue(graph.contains(22));
		assertTrue(graph.contains(20));
		assertTrue(graph.contains(23));
		
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
		
		assertEquals(8, graph.size());
	}
	
	private IntraproceduralPointerGraph createGraph(String className, String methodName, StringReference...references ) {
		DefUse defUse = new DefUse(getIR(className, methodName));
		
		Queue<StringReference> stringUses = Queues.newArrayDeque(Arrays.asList(references));
		
		IntraproceduralPointerGraph graph = IntraproceduralPointerGraph.create(defUse, stringUses);
		return graph;
	}
	
}
