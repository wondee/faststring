package de.unifrankfurt.faststring.analysis;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;

import de.unifrankfurt.faststring.analysis.classes.SimpleIfClass;

public class TestControlFlow {
	
	private static ClassReader reader;
	
	@Before
	public void setUp() throws IOException {
		reader = new ClassReader(SimpleIfClass.class.getName());
	}
	
	@Test
	public void testControlFlow() {
		reader.accept(new InternalControlFlowClassVisitor(), ClassReader.EXPAND_FRAMES);
	}
}
