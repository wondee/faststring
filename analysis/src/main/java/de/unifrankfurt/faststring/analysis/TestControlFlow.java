package de.unifrankfurt.faststring.analysis;

import static org.objectweb.asm.Opcodes.ASM5;

import java.io.IOException;
import java.util.Arrays;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class TestControlFlow {

	public static void main(String[] args) throws IOException {
		ClassReader reader = new ClassReader(
				"de.unifrankfurt.faststring.analysis.test.SingleStringMethod");
		
		reader.accept(new ClassVisitor(ASM5) {
			private String owner;
			@Override
			public void visit(int version, int access, String name, String signature,
					String superName, String[] interfaces) {
				owner = name;
				System.out.println(name);
				
			}
			
			@Override
			public MethodVisitor visitMethod(int access, String name,
					String desc, String signature, String[] exceptions) {
				System.out.printf("%d, %s, %s, %s, %s\n", access, name, desc, signature, Arrays.toString(exceptions));
				return new ControlFlowAdapter(owner, access, name, desc, null);
			}
			
		}, ClassReader.EXPAND_FRAMES);
	}
}
