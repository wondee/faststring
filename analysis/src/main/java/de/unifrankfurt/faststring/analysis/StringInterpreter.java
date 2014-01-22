package de.unifrankfurt.faststring.analysis;
import static de.unifrankfurt.faststring.analysis.util.ASMUtil.insnToString;

import java.util.List;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.SimpleVerifier;
import org.objectweb.asm.tree.analysis.Value;

public class StringInterpreter extends SimpleVerifier {
	
	private Map<Integer, String> stringVariables;

	public StringInterpreter(Map<Integer, String> strings) {
		
		this.stringVariables = strings;
		System.out.println("strings found: " + strings);
		
	}

	@Override
	public Value copyOperation(AbstractInsnNode insn, Value value)
			throws AnalyzerException {
		System.out.println("copyOperation: " + insnToString(insn));
		
		insn.accept(new MethodVisitor(ASM5) {
			@Override
			public void visitVarInsn(final int opcode, final int var)  {
				System.out.printf("visitVarInsn %d %d \n", opcode, var);
				if (stringVariables.containsKey(var)) {
					System.out.println("is a string variable");
				}
			}
			
		});
		
		
		return super.copyOperation(insn, (BasicValue) value);
	}
	
	@Override
	public Value merge(Value value1, Value value2) {
		System.out.println("merge: " + value1 + "; " + value2);
		
		return super.merge((BasicValue) value1, (BasicValue) value2);
	}
	
	@Override
	public BasicValue naryOperation(AbstractInsnNode insn, 
			@SuppressWarnings("rawtypes") final List values) throws AnalyzerException {
		System.out.println("naryOperation: " + insnToString(insn));
		
		if (insn.getOpcode() == INVOKEVIRTUAL) {
			
			insn.accept(new MethodVisitor(ASM5) {
				@Override
				public void visitMethodInsn(int opcode, String owner,
						String name, String desc) {
					System.out.printf("visitMethodInsn %d %s %s %s\n", opcode, owner, name, desc);
				}
				
			});
			
			System.out.println(values.size());
			
			
			for (Object v : values) {
				BasicValue value = (BasicValue) v;
				System.out.println(value);
			}
			
			
		}
		
		return super.naryOperation(insn, values);
	}
	
	@Override
	public Value binaryOperation(AbstractInsnNode insn, Value arg1, Value arg2)
			throws AnalyzerException {
		System.out.println("binaryOperation: " + insnToString(insn));
		return super.binaryOperation(insn, (BasicValue) arg1, (BasicValue) arg2);
	}
	
	
}
