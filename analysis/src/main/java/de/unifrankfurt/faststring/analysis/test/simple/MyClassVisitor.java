package de.unifrankfurt.faststring.analysis.test.simple;

import static org.objectweb.asm.Opcodes.ASM5;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class MyClassVisitor extends ClassVisitor{

	String owner;
	
	public MyClassVisitor() {
		super(ASM5, null);
	}

	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		owner = name;
		System.out.println(name);
		
	}
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		System.out.printf("%d, %s, %s, %s, %s\n", access, name, desc, signature, Arrays.toString(exceptions));
		return new MyMethodVisitor();
	}

	
}
