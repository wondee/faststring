package de.unifrankfurt.faststring.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.MethodEditor;
import com.ibm.wala.shrikeBT.analysis.Analyzer.FailureException;
import com.ibm.wala.shrikeBT.analysis.Verifier;

import de.unifrankfurt.faststring.analysis.graph.ConstantDefinition;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode.Visitor;
import de.unifrankfurt.faststring.analysis.graph.MethodCallInstruction;
import de.unifrankfurt.faststring.analysis.graph.ParameterDefinition;
import de.unifrankfurt.faststring.analysis.graph.PhiInstructionNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.graph.ReturnInstruction;
import de.unifrankfurt.faststring.transform.TransformationInfo.Constant;
import de.unifrankfurt.faststring.transform.patches.PatchFactory;


public class MethodTransformer {

	private static final Logger LOG = LoggerFactory.getLogger(MethodTransformer.class);

	private MethodData methodData;
	private MethodEditor editor;
	private PatchFactory patchFactory;
	private TransformationInfo transformationInfo;

	public MethodTransformer(MethodData methodData, TransformationInfo transformationInfo) {
		this.methodData = methodData;
		this.transformationInfo = transformationInfo;
		this.editor = new MethodEditor(methodData);
		this.patchFactory = new PatchFactory(transformationInfo, editor);
	}

	public void transformMethod() {
		LOG.info("editing: {}", methodData.getName());

		editor.beginPass();

		createConversations();

		try {
			new Verifier(methodData).verify();
		} catch (FailureException e) {
			throw new IllegalStateException(e);
		}

		editor.applyPatches();
		editor.endPass();

	}

	private void createConversations() {

//		for (Constant constant : transformationInfo.getConstants()) {
//			patchFactory.createConstantDefinition(constant);
//		}
//
//		for (Reference ref : transformationInfo.getDefinitionConversationsToOpt()) {
//
//			InstructionNode definition = ref.getDefinition();
//
//			definition.visit(visitor);
//
//			if (definition instanceof ParameterDefinition) {
//				for (Integer orgLocal : definition.getLocalVariableIndex(ref.valueNumber())) {
//					patchFactory.createOptConversation(orgLocal);
//				}
//			} else if (definition instanceof MethodCallInstruction) {
//				MethodCallInstruction callResultDef = (MethodCallInstruction) definition;
//
//				System.out.println("local for call definition: " + callResultDef);
//
//				for (Integer local : callResultDef.getLocalVariableIndex(ref.valueNumber())) {
//					patchFactory.createOptConversationFromStack(local, callResultDef.getByteCodeIndex());
//
//				}
////				callResultDef.
//			} else if (definition instanceof )
//		}


	}

	private class DefinitionConversationToOpt implements Visitor {

		@Override
		public void visitConstant(ConstantDefinition node) {
//			patchFactory.createConstantDefinition(constant);
		}

		@Override
		public void visitMethodCall(MethodCallInstruction node) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visitParameter(ParameterDefinition node) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visitPhi(PhiInstructionNode node) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visitReturn(ReturnInstruction node) {
			// TODO Auto-generated method stub

		}

	}

}
