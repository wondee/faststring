package de.unifrankfurt.faststring.analysis;

import static org.objectweb.asm.Opcodes.ASM5;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;


public class ControlFlowAdapter extends MethodVisitor {
	
	private static final String STRING = Type.getDescriptor(String.class);
	
	private String owner;
//	MethodVisitor next;

	private Map<Integer, String> strings = new HashMap<>(); 
	
	private ControlFlowGraph graph = null;
	
	public ControlFlowAdapter(String owner, int access, String name,
			String desc, MethodVisitor mv) {
		super(ASM5, new MethodNode(access, name, desc, null, null));

		this.owner = owner;
//		next = mv;
	}
	
	@Override
	public void visitLocalVariable(String name, String desc, String signature,
			Label start, Label end, int index) {
		System.out.printf("found local variable: %s, %s, %s, %s, %s, %d\n", name, desc, signature, start.toString(), end.toString(), index);
		
		if (Type.getObjectType(desc).getInternalName().equals(STRING)) {
			strings.put(index, name);
		}
	}
	
	@Override
	public void visitEnd() {
		
		MethodNode mn = (MethodNode) mv;
		ControlFlowAnalyzer analyzer = new ControlFlowAnalyzer(strings);
		try {
			graph = new ControlFlowGraph(analyzer.analyze(owner, mn), mn.instructions.toArray());
			graph.identifyStringOperations();
			
			
		} catch (AnalyzerException e) {
			throw new RuntimeException(e);
		}
	}

	public ControlFlowGraph getGraph() {
		return graph;
	}
}
