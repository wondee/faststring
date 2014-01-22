package de.unifrankfurt.faststring.analysis;

import java.util.Map;

import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.Frame;

public class ControlFlowAnalyzer extends Analyzer {

	public ControlFlowAnalyzer(Map<Integer, String> strings) {
		super(new StringInterpreter(strings));
	}

	protected Frame newFrame(int nLocals, int nStack) {
		return new Node(nLocals, nStack);
	}

	protected Frame newFrame(Frame src) {
		return new Node(src);
	}

	protected void newControlFlowEdge(int src, int dst) {
		Node s = (Node) getFrames()[src];
		s.add((Node) getFrames()[dst]);
	}

}
