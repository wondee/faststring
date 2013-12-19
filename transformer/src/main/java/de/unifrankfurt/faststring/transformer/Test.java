package de.unifrankfurt.faststring.transformer;

import java.io.IOException;
import java.util.Arrays;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;


public class Test {

	public static void main(String[] args) throws IOException {
		ClassReader reader = new ClassReader("de.unifrankfurt.faststring.transformer.GreetingService");
		
		reader.accept(new MyVistor(), 0);
		
	}

	public static class MyVistor extends EmptyVisitor {
		
		
		@Override
		public void visitMethodInsn(int opcode, String owner, String name,
				String desc) {
			System.out.printf("%d, %s, %s, %s\n", opcode, owner, name, desc);
			
			Type type = Type.getType(owner);
			
			System.out.println(type.getClassName());
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc,
				String signature, String[] exceptions) {
			System.out.printf("%d, %s, %s, %s, %s\n", access, name, desc, signature, Arrays.toString(exceptions));
			return this;
		}
	}
	
	
}
