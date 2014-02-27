package de.unifrankfurt.faststring.analysis;

import static de.unifrankfurt.faststring.analysis.util.ASMUtil.*;
import java.util.Map;


import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.SimpleVerifier;
import org.objectweb.asm.tree.analysis.Value;

public class StringInterpreter extends SimpleVerifier {
	
	private Map<Integer, String> stringVariables;

	public StringInterpreter(Map<Integer, String> strings) {
		this.stringVariables = strings;		
	}
	
	@Override
	public Value copyOperation(AbstractInsnNode insn, Value value)
			throws AnalyzerException {
		System.out.println("copyOperation: " + insnToString(insn));
		
		BasicValue bvalue = super.copyOperation(insn, (BasicValue) value);
		
		if (bvalue.isReference()) {
//			System.out.println("is a reference");
			
			StringVarIdentifier identifier = new StringVarIdentifier(stringVariables);
			insn.accept(identifier);
			
			if (identifier.foundVar()) {
//				System.out.println("creating new stringvalue");
				
				return new StringValue(identifier.getVar());
			}
			
		} 
		return bvalue;
		
		
	}
	
	@Override
	public Value binaryOperation(AbstractInsnNode insn, Value v1, Value v2)
			throws AnalyzerException {
		System.out.println("binaryOperation: " + insnToString(insn) + " " + v1 + " " +v2);
		return null;
	}
	
	@Override
	public Value ternaryOperation(AbstractInsnNode insn, Value v1,
			Value v2, Value v3) throws AnalyzerException {
		System.out.println("ternaryOperation: " + insnToString(insn) + " " + v1 +v2 + v3);
		return null;
	}
	
	@Override
	public Value unaryOperation(AbstractInsnNode insn, Value v1)
			throws AnalyzerException {
		System.out.println("unaryOperation: " + insnToString(insn) + " " + v1 );
		return null;
	}
	
	@Override
	public Value merge(Value value1, Value value2) {
		System.out.println("merge: " + value1 + " & " + value2);
		
		if (value1 instanceof StringValue || value2 instanceof StringValue) {
			
		}
		BasicValue merge = super.merge((BasicValue)value1, (BasicValue) value2);
		System.out.println("returns " + merge);
		return merge;
	}
		
}
