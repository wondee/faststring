package de.unifrankfurt.faststring.analysis.graph;

import static de.unifrankfurt.faststring.analysis.utils.TestUtilities.assertList;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Iterables;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;

import de.unifrankfurt.faststring.analysis.BaseAnalysisTest;
import de.unifrankfurt.faststring.analysis.IRMethod;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraphBuilder;
import de.unifrankfurt.faststring.analysis.label.BuiltInTypes;
import de.unifrankfurt.faststring.analysis.model.CallResultDefinition;
import de.unifrankfurt.faststring.analysis.model.ConstantDefinition;
import de.unifrankfurt.faststring.analysis.model.PhiDefinition;
import de.unifrankfurt.faststring.analysis.util.GraphUtil;

public class TestDataFlowGraph extends BaseAnalysisTest {

	
	@Test
	public void testPhiLoop() {
		
		DataFlowGraph graph = createGraph("MethodAnalyzerTestClass", "phiLoop");
		
		assertTrue(graph.contains(4));
		assertTrue(graph.contains(5));
		assertTrue(graph.contains(7));
		assertTrue(graph.contains(10));
		assertTrue(graph.contains(11));
		
		assertEquals(5, graph.size());

		assertThat(graph.get(5).getDef(), instanceOf(ConstantDefinition.class));
		assertThat(graph.get(10).getDef(), instanceOf(CallResultDefinition.class));
		assertThat(graph.get(7).getDef(), instanceOf(PhiDefinition.class));
		
		assertList(getStartingPointsAsInts(graph), 7);
	}

	@Test
	public void testSimpleIfSubstring() {
		DataFlowGraph graph = createGraph("MethodAnalyzerTestClass", "simpleIfSubstring");

		
		assertTrue(graph.contains(4));
		assertTrue(graph.contains(5));
		assertTrue(graph.contains(9));
		assertTrue(graph.contains(16));
		assertTrue(graph.contains(22));
		assertTrue(graph.contains(20));
		assertTrue(graph.contains(23));
		
		
		assertEquals(7, graph.size());
		
		assertThat(graph.get(4).getDef(), instanceOf(ConstantDefinition.class));
		assertThat(graph.get(9).getDef(), instanceOf(CallResultDefinition.class));
		assertThat(graph.get(20).getDef(), instanceOf(CallResultDefinition.class));
		

		assertList(getStartingPointsAsInts(graph), 4, 5);
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
		
		assertEquals(9, graph.size());
		

		assertThat(graph.get(4).getDef(), instanceOf(ConstantDefinition.class));
		assertThat(graph.get(5).getDef(), instanceOf(ConstantDefinition.class));
		assertThat(graph.get(12).getDef(), instanceOf(PhiDefinition.class));
		assertThat(graph.get(19).getDef(), instanceOf(CallResultDefinition.class));
		
		assertList(getStartingPointsAsInts(graph), 7, 8, 12);
	}

	@Test
	public void testParamDef() {
		DataFlowGraph graph = createGraph("MethodAnalyzerTestClass", "paramDef");

		assertTrue(graph.contains(2));
		assertTrue(graph.contains(6));
		assertTrue(graph.contains(10));
		
		assertEquals(3, graph.size());
		assertList(getStartingPointsAsInts(graph), 2, 6);
	}
	
	private Iterable<Integer> getStartingPointsAsInts(DataFlowGraph graph) {
		return Iterables.transform(graph.getAllLabelMatchingReferences(), GraphUtil.stringReferenceToInt);
	}
	
	
	private DataFlowGraph createGraph(String className, String methodName) {
		
		IR ir = getIR(className, methodName);
		DefUse defUse = new DefUse(ir);
		
		return new DataFlowGraphBuilder(BuiltInTypes.SUBSTRING, new IRMethod(ir, defUse)).createDataFlowGraph();
	}
	
}
