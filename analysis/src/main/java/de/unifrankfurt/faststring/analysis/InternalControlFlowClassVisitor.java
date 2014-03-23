package de.unifrankfurt.faststring.analysis;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class InternalControlFlowClassVisitor extends ClassVisitor {

	private String owner;

	public InternalControlFlowClassVisitor() {
		super(ASM5);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		owner = name;
		System.out.println("analysing class " + name);
		
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name,
			String desc, String signature, String[] exceptions) {
//		System.out.printf("analysing %d, %s, %s, %s, %s\n", access, name, desc, signature, Arrays.toString(exceptions));
		System.out.println("--- start analysing method " + name + " " + signature);
		return new ControlFlowAdapter(owner, access, name, desc, null);
	}
}
