package de.unifrankfurt.faststring.analysis;

import java.util.Arrays;
import java.util.Map;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
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


	public Node[] analyze(String owner, MethodNode m) throws AnalyzerException {
		Frame[] frames = super.analyze(owner, m);
		
		return Arrays.copyOf(frames, frames.length, Node[].class);
		
	}
	
}
