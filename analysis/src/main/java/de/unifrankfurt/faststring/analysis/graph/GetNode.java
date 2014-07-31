package de.unifrankfurt.faststring.analysis.graph;

public class GetNode extends NotLabelableNode {

	public GetNode(int def) {
		setDef(def);
	}

	@Override
	public void visit(Visitor visitor) {
		visitor.visitGet(this);
	}

}
