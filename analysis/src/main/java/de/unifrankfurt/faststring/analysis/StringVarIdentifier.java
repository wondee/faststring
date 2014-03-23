package de.unifrankfurt.faststring.analysis;

import static org.objectweb.asm.Opcodes.ASM5;

import java.util.Map;

import org.objectweb.asm.MethodVisitor;

public class StringVarIdentifier extends MethodVisitor {
	private Map<Integer, String> strings;

	private Integer stringVar = null;
	
	public StringVarIdentifier(Map<Integer, String> strings) {
		super(ASM5);
		this.strings = strings;
	}
	
	@Override
	public void visitVarInsn(final int opcode, final int var)  {
//		System.out.printf("visitVarInsn %d %d %s\n", opcode, var, strings.containsKey(var));
		if (strings.containsKey(var)) {
//			System.out.println("is a string variable");
			stringVar = var;
		} else {
			stringVar = null;
		}
	}
	
	public boolean foundVar() {
		return stringVar != null;
	}
	
	public Integer getVar() {
		return stringVar;
	}
}
