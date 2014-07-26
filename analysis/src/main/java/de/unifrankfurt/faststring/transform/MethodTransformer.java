package de.unifrankfurt.faststring.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.MethodEditor;
import com.ibm.wala.shrikeBT.analysis.Analyzer.FailureException;
import com.ibm.wala.shrikeBT.analysis.Verifier;

import de.unifrankfurt.faststring.analysis.graph.ConstantNode;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode.Visitor;
import de.unifrankfurt.faststring.analysis.graph.GetNode;
import de.unifrankfurt.faststring.analysis.graph.MethodCallNode;
import de.unifrankfurt.faststring.analysis.graph.NewNode;
import de.unifrankfurt.faststring.analysis.graph.ParameterNode;
import de.unifrankfurt.faststring.analysis.graph.PhiNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.graph.ReturnNode;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.transform.patches.ConversationPatchFactory;


public class MethodTransformer {

	private static final Logger LOG = LoggerFactory.getLogger(MethodTransformer.class);

	private MethodData methodData;
	MethodEditor editor;
	TransformationInfo transformationInfo;

	public MethodTransformer(MethodData methodData, TransformationInfo transformationInfo) {
		this.methodData = methodData;
		this.transformationInfo = transformationInfo;
		this.editor = new MethodEditor(methodData);
	}

	public void transformMethod() {
		LOG.info("editing: {}", methodData.getName());

		editor.beginPass();

		for (Reference ref : transformationInfo.getReferences()) {

			createDefinitionConversations(ref.getDefinition(), ref);

			for (InstructionNode use : ref.getUses()) {
				createUseOptimization(ref, use);
				createUseConversations(ref, use);
			}

		}

		try {
			new Verifier(methodData).verify();
			editor.applyPatches();
			editor.endPass();

		} catch (FailureException e) {
			throw new IllegalStateException(e);
		}
	}

	private void createUseConversations(Reference ref, InstructionNode use) {
		if (!use.isCompatibleWith(ref)) {
			Converter converter = new UseConverter(ref.getLabel(), use.getLabel());
			Collection<Integer> locals = use.getLocals(ref.valueNumber());
			if (!locals.isEmpty()) {
				for (Integer local : locals) {
					converter.setLocal(transformationInfo.getLocalForLabel(null, ref.getLabel(), local));
//					converter.setLocal(local);
					use.visit(converter);

				}
			} else {
				converter.setLocal(-1);
				use.visit(converter);
			}
		}

	}

	private void createUseOptimization(Reference ref, InstructionNode use) {
		if (use.getLabel()!= null) {
			use.visit(new Optimizer(ref.valueNumber(),
					new ConversationPatchFactory(transformationInfo, editor, use.getLabel())));
		}
	}

	private void createDefinitionConversations(InstructionNode instructionNode, Reference ref) {
		if (!instructionNode.isCompatibleWith(ref)) {
			Converter converter = new DefinitionConverter(instructionNode.getLabel(), ref.getLabel());
			Collection<Integer> locals = instructionNode.getLocals(ref.valueNumber());
			if (!locals.isEmpty()) {
				for (Integer local : locals) {
					converter.setLocal(local);
					instructionNode.visit(converter);

				}
			} else {
				converter.setLocal(-1);
				instructionNode.visit(converter);
			}
		}

	}

	private abstract class Converter extends Visitor {
		int local = -1;
		ConversationPatchFactory patchFactory;

		public Converter(TypeLabel from, TypeLabel to) {
			this.patchFactory = new ConversationPatchFactory(transformationInfo, editor, from, to);
		}

		void setLocal(int local) {
			this.local = local;
		}
	}

	private class DefinitionConverter extends Converter {

		public DefinitionConverter(TypeLabel from, TypeLabel to) {
			super(from, to);
		}

		@Override
		public void visitConstant(ConstantNode node) {
			patchFactory.createConversationAfter(local, node.getByteCodeIndex());
		}

		@Override
		public void visitMethodCall(MethodCallNode callResultDef) {
			patchFactory.createConversationAfter(local, callResultDef.getByteCodeIndex());
		}

		@Override
		public void visitParameter(ParameterNode node) {
			patchFactory.createConversationAtStart(local);
		}

		@Override
		public void visitGet(GetNode node) {
			patchFactory.createConversationAfter(node.getByteCodeIndex());
		}

		@Override
		public void visitPhi(PhiNode node) {
			//TODO to be implemented

			// phi defs have merged locals additionally to the locals of their uses
			// can those locals which are taken from the uses be removed from the def locals??

			// first try: just convert on stack. Should be right when no store appears afterwards
			//

			patchFactory.createConversationBefore(local, node.getByteCodeIndex());
		}

		@Override
		public void visitReturn(ReturnNode node) {
			throw new UnsupportedOperationException("a return does not have a definition");
		}

		@Override
		public void visitNew(NewNode newNode) {
			patchFactory.replaceNew(newNode);
		}

	}

	private class UseConverter extends Converter {

		TypeLabel from;

		public UseConverter(TypeLabel from, TypeLabel to) {
			super(from, to);
			this.from = from;
		}

		@Override
		public void visitReturn(ReturnNode node) {
			createConversation(node);

		}

		@Override
		public void visitMethodCall(MethodCallNode node) {
			createConversation(node);
		}


		private void createConversation(InstructionNode node) {
			if (local != -1) {
				int orgLocal = transformationInfo.getOrgLocalForLabel(from, local);
				patchFactory.replaceLoad(orgLocal, node.getLoad(orgLocal), from);
			}
			patchFactory.createConversationBefore(node.getByteCodeIndex());
		}

	}

}
