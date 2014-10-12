package de.unifrankfurt.faststring.analysis.graph;

import static de.unifrankfurt.faststring.analysis.test.util.TestUtilities.assertList;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Iterables;

import de.unifrankfurt.faststring.analysis.test.util.TestUtilities;
import de.unifrankfurt.faststring.analysis.util.GraphUtil;
import de.unifrankfurt.faststring.utils.BaseAnalysisTest;

public class TestDataFlowGraph extends BaseAnalysisTest {


	@Test
	public void testPhiLoop() {

		DataFlowGraph graph = createGraph("MethodAnalyzerTestClass", "phiLoop");

		assertTrue(graph.contains(4));
		assertTrue(graph.contains(5));
		assertTrue(graph.contains(8));
		assertTrue(graph.contains(11));
		assertTrue(graph.contains(12));

		assertEquals(6, graph.size());

		assertThat(graph.get(5).getDefinition(), instanceOf(ConstantNode.class));
		assertThat(graph.get(11).getDefinition(), instanceOf(MethodCallNode.class));
		assertThat(graph.get(8).getDefinition(), instanceOf(PhiNode.class));

		assertList(getStartingPointsAsInts(graph), 8);
	}

	@Test
	public void testSimpleIfSubstring() {
		DataFlowGraph graph = createGraph("MethodAnalyzerTestClass", "simpleIfSubstring");


		assertTrue(graph.contains(4));
		assertTrue(graph.contains(5));
		assertTrue(graph.contains(9));
		assertTrue(graph.contains(17));
		assertTrue(graph.contains(21));
		assertTrue(graph.contains(24));
		assertTrue(graph.contains(25));


		assertEquals(14, graph.size());

		assertThat(graph.get(4).getDefinition(), instanceOf(ConstantNode.class));
		assertThat(graph.get(9).getDefinition(), instanceOf(MethodCallNode.class));
		assertThat(graph.get(24).getDefinition(), instanceOf(MethodCallNode.class));


		assertList(getStartingPointsAsInts(graph), 4, 5);
	}


	@Test
	@Ignore
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

		assertThat(graph.get(4).getDefinition(), instanceOf(ConstantNode.class));
		assertThat(graph.get(5).getDefinition(), instanceOf(ConstantNode.class));
		assertThat(graph.get(12).getDefinition(), instanceOf(PhiNode.class));
		assertThat(graph.get(19).getDefinition(), instanceOf(MethodCallNode.class));

		assertList(getStartingPointsAsInts(graph), 7, 8, 12);
	}

	@Test
	public void testParamDef() {
		DataFlowGraph graph = createGraph("MethodAnalyzerTestClass", "paramDef");

		assertTrue(graph.contains(2));
		assertTrue(graph.contains(6));
		assertTrue(graph.contains(10));

		assertEquals(6, graph.size());
		assertList(getStartingPointsAsInts(graph), 2, 6);
	}

	private Iterable<Integer> getStartingPointsAsInts(DataFlowGraph graph) {
		return Iterables.transform(graph.getAllLabelMatchingReferences(), GraphUtil.referenceToInt);
	}


	private DataFlowGraph createGraph(String className, String methodName) {

		return new DataFlowGraphBuilder(getIRMethod(className, methodName)).createDataFlowGraph(TestUtilities.loadTestLabel("SubstringString"));
	}

}
