package de.unifrankfurt.faststring.transform;

import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.MethodCallNode;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.transform.patches.ConversationPatchFactory;

class Optimizer extends InstructionNode.Visitor {

	private int v;

	private ConversationPatchFactory patchFactory;

	public Optimizer(int v, ConversationPatchFactory patchFactory) {
		this.v = v;
		this.patchFactory = patchFactory;
	}

	@Override
	public void visitMethodCall(MethodCallNode node) {

		if (node.isReceiver(v)) {

			updateLoads(node);
			updateStores(node);

			patchFactory.replaceMethodCall(node);

		}
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