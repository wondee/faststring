package de.unifrankfurt.faststring.analysis;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class MyMethodVisitor extends MethodVisitor{

	public MyMethodVisitor() {
		super(Opcodes.ASM5, null);
	}

	
    @Override
    public void visitMethodInsn(int opcode, String owner, String name,
                    String desc) {
            System.out.printf("%d, %s, %s, %s\n", opcode, owner, name, desc);
            
            Type type = Type.getObjectType(owner);
            
            System.out.println(type.getClassName());
    }

	
}
