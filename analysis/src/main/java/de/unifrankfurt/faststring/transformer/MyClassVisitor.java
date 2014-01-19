package de.unifrankfurt.faststring.transformer;

import static org.objectweb.asm.Opcodes.ASM5;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class MyClassVisitor extends ClassVisitor{

	public MyClassVisitor() {
		super(ASM5, null);
	}

	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		
		System.out.println(name);
		
	}
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		System.out.printf("%d, %s, %s, %s, %s\n", access, name, desc, signature, Arrays.toString(exceptions));
		return new MyMethodVisitor();
	}
	
}
