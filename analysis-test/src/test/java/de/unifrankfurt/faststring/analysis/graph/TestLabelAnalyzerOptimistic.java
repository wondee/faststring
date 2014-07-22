package de.unifrankfurt.faststring.analysis.graph;

import static de.unifrankfurt.faststring.analysis.test.util.TestUtilities.assertList;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import de.unifrankfurt.faststring.analysis.LabelAnalyzer;
import de.unifrankfurt.faststring.analysis.OptimisticStrategy;
import de.unifrankfurt.faststring.core.label.BuiltInTypes;

@Ignore
public class TestLabelAnalyzerOptimistic {
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
		
		System.out.println(graph);
		
		assertNotNull(graph.get(2).getLabel());
		assertNotNull(graph.get(3).getLabel());
		assertNotNull(graph.get(4).getLabel());
		assertNotNull(graph.get(5).getLabel());
		
		assertList(graph.get(1).getUseConversionsToOpt(BuiltInTypes.SUBSTRING), 0);
		assertTrue(graph.get(2).isDefinitionConversionToOpt(BuiltInTypes.SUBSTRING));
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

	@Test
	public void testMultipleInvokes() throws Exception {
		DataFlowGraph graph = analyze(
				new DataFlowTestBuilder()
			.parameterDefinition(1)
			
			.labelUse(1, 2)
			.labelUse(2, 3)
			.return_(3)
			.create()
			);
		
		assertNotNull(graph.get(1).getLabel());
		assertNotNull(graph.get(2).getLabel());
		assertNotNull(graph.get(3).getLabel());
	
//		assertList(graph.get(1).getUseConversionsToOpt(BuiltInTypes.SUBSTRING), 0);
//		assertList(graph.get(2).getUseConversionsToOpt(BuiltInTypes.SUBSTRING), 0);
//		assertList(graph.get(6).getUseConversionsFromOpt(BuiltInTypes.SUBSTRING), 0);
	}

		
	
	private DataFlowGraph analyze(DataFlowGraph graph) {
		LabelAnalyzer.analyzeLabel(graph, new OptimisticStrategy());
		return graph;
	}
	
}
