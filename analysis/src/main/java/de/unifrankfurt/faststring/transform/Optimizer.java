package de.unifrankfurt.faststring.transform;

import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.MethodCallNode;
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
		for (int local : node.getDefLocals()) {
		
			Integer storeIndex = node.getStore(local);
			if (storeIndex != null) {
				patchFactory.replaceStore(local, storeIndex, node.getLabel());
			}
		}
	}

	private void updateLoads(MethodCallNode node) {
		for (int local : node.getLocals(v)) {
			
			Integer loadIndex = node.getLoad(local);
			if (loadIndex != null) {
				patchFactory.replaceLoad(local, loadIndex, node.getLabel());
			}
		}
	}



}