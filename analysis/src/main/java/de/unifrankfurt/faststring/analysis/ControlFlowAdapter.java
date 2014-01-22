package de.unifrankfurt.faststring.analysis;

import static org.objectweb.asm.Opcodes.ASM5;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;

public class ControlFlowAdapter extends MethodVisitor {
	
	private static final String STRING = Type.getDescriptor(String.class);
	
	String owner;
	MethodVisitor next;

	Map<Integer, String> strings = new HashMap<>(); 
	
	public ControlFlowAdapter(String owner, int access, String name,
			String desc, MethodVisitor mv) {
		super(ASM5, new MethodNode(access, name, desc, null, null));

		this.owner = owner;
		next = mv;
	}
	
	@Override
	public void visitLocalVariable(String name, String desc, String signature,
			Label start, Label end, int index) {
		System.out.printf("visit local variable: %s, %s, %s, %s, %s, %d\n", name, desc, signature, start.toString(), end.toString(), index);
		
		
		if (Type.getObjectType(desc).getInternalName().equals(STRING)) {
			strings.put(index, name);
		}
	}
	
	@Override
	public void visitEnd() {
		MethodNode mn = (MethodNode) mv;
		ControlFlowAnalyzer analyzer = new ControlFlowAnalyzer(strings);
		
		try {
			Frame[] frames =  analyzer.analyze(owner, mn);
			
			for (Frame frame : frames) {
				Node node = (Node) frame;
//				System.out.println(node);
			}
			
			
		} catch (AnalyzerException e) {
			throw new RuntimeException(e);
		}
	}
}
