package de.unifrankfurt.faststring.analysis;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.tree.analysis.Frame;

public class Node extends Frame {

	private Set<Node> successors = new HashSet<>();

	public Node(int nLocals, int nStack) {
		super(nLocals, nStack);
	}

	public Node(Frame src) {
		super(src);
	}

	public void add(Node n) {
		successors.add(n);
	}

	public Set<Node> successors() {
		return successors;
		
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
}
