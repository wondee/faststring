package de.unifrankfurt.faststring.analysis.graph;

import static org.junit.Assert.*;
import static de.unifrankfurt.faststring.utils.TestUtilities.*;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.CountingStrategy;
import de.unifrankfurt.faststring.analysis.LabelAnalyzer;
import de.unifrankfurt.faststring.analysis.label.BuiltInTypes;

public class TestLabelAnalyzerCounting {

	@Test
	public void testSimple() throws Exception {
		DataFlowGraph graph = analyze(new DataFlowTestBuilder()
			.parameterDefinition(3)
			.labelUse(3, 4)
			.return_(4)
			.create());

		assertNotNull(graph.get(3).getLabel());
		assertNotNull(graph.get(4).getLabel());

		assertTrue(graph.get(3).isDefinitionConversionToOpt(BuiltInTypes.SUBSTRING));
		assertList(graph.get(4).getUseConversionsFromOpt(BuiltInTypes.SUBSTRING), 0);
	}

	@Test
	public void testPhi() throws Exception {
		DataFlowGraph graph = analyze(new DataFlowTestBuilder()
			.parameterDefinition(1)
			.parameterDefinition(2)
			.labelUse(2, 3)
			.phi(4, 1, 3)
			.labelUse(4, 5)
			.create());

		assertNotNull(graph.get(2).getLabel());
		assertNotNull(graph.get(3).getLabel());
		assertNotNull(graph.get(4).getLabel());
		assertNotNull(graph.get(5).getLabel());

		assertList(graph.get(1).getUseConversionsToOpt(BuiltInTypes.SUBSTRING), 0);
		assertTrue(graph.get(2).isDefinitionConversionToOpt(BuiltInTypes.SUBSTRING));
		assertFalse(graph.get(2).isDefinitionConversionFromOpt(BuiltInTypes.SUBSTRING));
	}

	@Test
	public void testPhiWithLoop() throws Exception {
		DataFlowGraph graph = analyze(
				new DataFlowTestBuilder()
			.parameterDefinition(1)
			.parameterDefinition(2)
			.phi(3, 2, 5)
			.phi(4, 1, 3)
			.labelUse(4, 5)
			.labelUse(3, 6)
			.return_(6)
			.create()
			);

		assertNotNull(graph.get(3).getLabel());
		assertNotNull(graph.get(4).getLabel());
		assertNotNull(graph.get(5).getLabel());
		assertNotNull(graph.get(6).getLabel());

		assertList(graph.get(1).getUseConversionsToOpt(BuiltInTypes.SUBSTRING), 0);
		assertList(graph.get(2).getUseConversionsToOpt(BuiltInTypes.SUBSTRING), 0);
		assertList(graph.get(6).getUseConversionsFromOpt(BuiltInTypes.SUBSTRING), 0);
	}

	private DataFlowGraph analyze(DataFlowGraph graph) {
		LabelAnalyzer.analyzeLabel(graph, new CountingStrategy());
		return graph;
	}

}
