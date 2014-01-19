package de.unifrankfurt.faststring.analysis;

import java.io.IOException;

import org.objectweb.asm.ClassReader;

public class Test {

	public static void main(String[] args) throws IOException {
		ClassReader reader = new ClassReader(
				"de.unifrankfurt.faststring.transformer.GreetingService");
		
		reader.accept(new MyClassVisitor(), 0);
		
	}

	
	
	
}
