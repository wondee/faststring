package de.unifrankfurt.faststring.analysis.graph;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import de.unifrankfurt.faststring.analysis.LabelAnalyzer;
import de.unifrankfurt.faststring.analysis.OptimisticStrategy;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.test.util.TestUtilities;

@Ignore
public class TestLabelAnalyzerOptimistic {
	
	private static final TypeLabel LABEL = TestUtilities.loadTestLabel("SubstringString");
	
	
	@Test
	public void testSimple() throws Exception {
		DataFlowGraph graph = analyze(new DataFlowTestBuilder(LABEL)
			.parameterDefinition(3)
			.labelUse(3, 4)
			.return_(4)
			.create());

		assertNotNull(graph.get(3).getLabel());
		assertNotNull(graph.get(4).getLabel());

		assertTrue(graph.get(3).isLabel(LABEL));
		assertFalse(graph.get(3).getDefinition() instanceof LabelableNode);

		assertTrue(graph.get(4).isLabel(LABEL));
		assertFalse(graph.get(3).getUses().get(0) instanceof LabelableNode);
	}

	@Test
	public void testPhi() throws Exception {
		DataFlowGraph graph = analyze(new DataFlowTestBuilder(LABEL)
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

		
		
		assertTrue(graph.get(1).isLabel(null));
		assertThat(graph.get(1).getUses().get(0), instanceOf(LabelableNode.class));
		assertTrue(((LabelableNode)graph.get(1).getUses().get(0)).isLabel(LABEL));
		
		assertTrue(graph.get(2).isLabel(LABEL));
		assertFalse(graph.get(2).getDefinition() instanceof LabelableNode);
		
	}

	@Test
	public void testPhiWithLoop() throws Exception {
		DataFlowGraph graph = analyze(
				new DataFlowTestBuilder(LABEL)
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

		assertTrue(graph.get(1).isLabel(null));
		assertThat(graph.get(1).getUses().get(0), instanceOf(LabelableNode.class));
		assertTrue(((LabelableNode)graph.get(1).getUses().get(0)).isLabel(LABEL));
		
		assertTrue(graph.get(2).isLabel(null));
		assertThat(graph.get(2).getUses().get(0), instanceOf(LabelableNode.class));
		assertTrue(((LabelableNode)graph.get(2).getUses().get(0)).isLabel(LABEL));
		
		assertTrue(graph.get(1).isLabel(LABEL));
		assertFalse(graph.get(1).getUses().get(0) instanceof LabelableNode);
		
	}

	@Test
	public void testMultipleInvokes() throws Exception {
		DataFlowGraph graph = analyze(
				new DataFlowTestBuilder(LABEL)
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
