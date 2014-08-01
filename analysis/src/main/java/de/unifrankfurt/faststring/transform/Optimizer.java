package de.unifrankfurt.faststring.transform;

import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.MethodCallNode;
import de.unifrankfurt.faststring.analysis.graph.NewNode;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.transform.patches.ConversionPatchFactory;

class Optimizer extends InstructionNode.MustCallVisitor {

	private ConversionPatchFactory patchFactory;

	public Optimizer(ConversionPatchFactory patchFactory) {
		this.patchFactory = patchFactory;
	}

	@Override
	public void visitMethodCall(MethodCallNode node) {
		updateLoads(node);
		updateStores(node);

		patchFactory.replaceMethodCall(node);
	}
	
	@Override
	public void visitNew(NewNode newNode) {
		patchFactory.replaceNew(newNode);
	}
	
	private void updateStores(MethodCallNode node) {
		TypeLabel defLabel = node.getDefLabel();
		if (defLabel != null) {
			for (int local : node.getDefLocals()) {

				Integer storeIndex = node.getStore();
				if (storeIndex != null) {
					patchFactory.replaceStore(local, storeIndex, defLabel);
				}
			}
		}
	}

	private void updateLoads(MethodCallNode node) {

		for (int i = 0; i < node.getParams().size() + 1; i++) {
			int v = node.getUse(i);

			TypeLabel useLabel = node.getLabelForUse(v);

			if (useLabel != null) {

				for (int local : node.getLocals(v)) {
					patchFactory.replaceLoad(local, node.getLoad(i), useLabel);

				}
			}


		}
	}



}