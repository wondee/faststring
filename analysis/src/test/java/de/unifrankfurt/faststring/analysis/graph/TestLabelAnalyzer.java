package de.unifrankfurt.faststring.analysis.graph;

import static org.junit.Assert.*;
import static de.unifrankfurt.faststring.analysis.utils.TestUtilities.*;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.LabelAnalyzer;

public class TestLabelAnalyzer {

	@Test
	public void testSimple() throws Exception {
		DataFlowGraph graph = new DataFlowTestBuilder()
			.addParameterDefinition(3)
			.addLabelUse(3, 4)
			.create();
		
		LabelAnalyzer.analyzeLabel(graph);
		
		
		assertNotNull(graph.get(3).getLabel());
		assertNotNull(graph.get(4).getLabel());
		
		assertTrue(graph.get(3).isDefinitionConversionToOpt());
	}

	@Test
	public void testPhi() throws Exception {
		DataFlowGraph graph = new DataFlowTestBuilder()
			.addParameterDefinition(1)
			.addParameterDefinition(2)
			.addLabelUse(2, 3)
			.addPhi(4, 1, 3)
			.addLabelUse(4, 5)
			.create();
	
		LabelAnalyzer.analyzeLabel(graph);
		
		assertNotNull(graph.get(2).getLabel());
		assertNotNull(graph.get(3).getLabel());
		assertNotNull(graph.get(4).getLabel());
		assertNotNull(graph.get(5).getLabel());
		
		assertTrue(graph.get(2).isDefinitionConversionToOpt());
		assertList(graph.get(1).getUseConversionsToOpt(), 0);
	}
	
}
