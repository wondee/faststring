package de.unifrankfurt.faststring.analysis;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.utils.BaseTransformerTest;

public class TestLocateLoad extends BaseTransformerTest {


	@Test
	public void test() throws Exception {
		AnalysisResult result = analyzeToResult("test");
		
		Reference ref = findRef(result, 6);
		
		InstructionNode substring = ref.getDefinition();
		InstructionNode startsWith = ref.getUses().get(0);
		
		assertNotNull(substring.getLoad(1));
		assertNotNull(startsWith.getLoad(1));
		
		assertEquals(3, (int)substring.getLoad(1));
		assertEquals(4, (int)startsWith.getLoad(1));
		

	}

	private Reference findRef(AnalysisResult result, int i) {
		for (Reference ref : result.getRefs()) {
			if (ref.valueNumber() == i) {
				return ref;
			}
		}
		throw new NoSuchElementException("could not find " + i + " in " + result);
	}

	@Override
	public String getTestClass() {
		return "LoadTestClass";
	}
}
