package de.unifrankfurt.faststring.analysis;

import java.util.Arrays;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MyMethodVisitor extends MethodVisitor{

	public MyMethodVisitor() {
		super(Opcodes.ASM5, null);
	}

	@Override
	public void visitFrame(int type, int nLocal, Object[] local, int nStack,
			Object[] stack) {
		System.out.printf("visit frame: %d, %d, %s, %d, %s\n", type, nLocal, Arrays.toString(local), nStack, Arrays.toString(stack));
	}
	
	@Override
	public void visitLocalVariable(String name, String desc, String signature,
			Label start, Label end, int index) {
		System.out.printf("visit local variable: %s, %s, %s, %s, %s, %d\n", name, desc, signature, start.toString(), end.toString(), index);
	}
	
    @Override
    public void visitMethodInsn(int opcode, String owner, String name,
                    String desc) {
            System.out.printf("visit method instruction: %d, %s, %s, %s\n", opcode, owner, name, desc);
            
//            Type type = Type.getObjectType(owner);
//            
//            System.out.println(type.getClassName());
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
    	System.out.printf("visit var instruction: %d, %d\n", opcode, var);
    }
    
    
}
