package de.unifrankfurt.faststring.analysis;

import static org.objectweb.asm.Opcodes.ASM5;

import org.objectweb.asm.MethodVisitor;

public class StringMethodIdentifier extends MethodVisitor{
	
//	private Frame currentFrame;

	private String name = null;
	
	public StringMethodIdentifier() {
		super(ASM5);
		
//		this.currentFrame = currentFrame;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner,
			String name, String desc) {
//		System.out.printf("identified %d %s %s %s\n", opcode, owner, name, Arrays.toString(Type.getArgumentTypes(desc)));
		
		this.name = name;
	}

	public boolean isStringCall() {
		return name != null;
	}
	
	public String getName() {
		return name;
	}
	
}
