package de.unifrankfurt.faststring.analysis;

import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.Value;

public class ControlFlowGraph {

	private Node[] nodes;
	
	private AbstractInsnNode[] insns;
	

	public ControlFlowGraph(Node[] nodes, AbstractInsnNode[] insns) {
		super();
		this.nodes = nodes;
		this.insns = insns;
	}

	public void identifyStringOperations() {
		
		
		
		for (int i = 0; i < insns.length; i++) {
			AbstractInsnNode insn = insns[i];
			
			
									
			if (insn.getOpcode() == INVOKEVIRTUAL) {
				StringMethodIdentifier identifier = new StringMethodIdentifier();
				insn.accept(identifier);					
				if (identifier.isStringCall()) {
					Value value = nodes[i].getStack(0);
					System.out.println("invoke virtual on string " + value.getClass());
					if (value instanceof StringValue) {
						System.out.println("call " + identifier.getName() + 
								" on local string variable " + 
								((StringValue)value).getIndex());
						
					}
				}
			}				
		}	
	}
	
	
	
}
