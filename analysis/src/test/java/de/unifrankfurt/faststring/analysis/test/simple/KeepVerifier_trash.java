package de.unifrankfurt.faststring.analysis.test.simple;

public class KeepVerifier_trash {


//	@Override
//	public Value merge(Value value1, Value value2) {
//		System.out.println("merge: " + value1 + "; " + value2);
//		
//		return super.merge((BasicValue) value1, (BasicValue) value2);
//	}
//	
//	@Override
//	public BasicValue naryOperation(AbstractInsnNode insn, 
//			@SuppressWarnings("rawtypes") final List values) throws AnalyzerException {
//		System.out.println("naryOperation: " + insnToString(insn));
//		
//		if (insn.getOpcode() == INVOKEVIRTUAL) {
//			
//			insn.accept(new MethodVisitor(ASM5) {
//				@Override
//				public void visitMethodInsn(int opcode, String owner,
//						String name, String desc) {
//					System.out.printf("visitMethodInsn %d %s %s %s\n", opcode, owner, name, desc);
//				}
//				
//			});
//			
//			System.out.println(values.size());
//			
//			
//			for (Object v : values) {
//				BasicValue value = (BasicValue) v;
//				System.out.println(value);
//			}
//			
//			
//		}
//		
//		return super.naryOperation(insn, values);
//	}
//	
//	@Override
//	public Value binaryOperation(AbstractInsnNode insn, Value arg1, Value arg2)
//			throws AnalyzerException {
//		System.out.println("binaryOperation: " + insnToString(insn));
//		return super.binaryOperation(insn, (BasicValue) arg1, (BasicValue) arg2);
//	}
//	

}
