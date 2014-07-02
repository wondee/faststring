package de.unifrankfurt.faststring.transform;


public class MethodTransformer {

//	public void transformMethod(MethodData methodData, TransformationInfo transformationInfo) {
//		System.out.println("editing: " + methodData.getName());
//
//
//		MethodEditor editor = new MethodEditor(methodData);
//		editor.beginPass();
//
//		PatchFactory patchFactory = new PatchFactory(transformationInfo, editor);
//
//		createConstants(patchFactory, editor, transformationInfo);
//
//
////		editor.insertBefore(i, p);
//
//		try {
//			new Verifier(methodData).verify();
//		} catch (FailureException e) {
//			throw new IllegalStateException(e);
//		}
//
//		editor.applyPatches();
//		editor.endPass();
//
//	}
//
//	private void createConstants(PatchFactory patchFactory, MethodEditor editor, TransformationInfo transformationInfo) {
//
//
//		for (Constant constant : transformationInfo.getConstants()) {
//			patchFactory.createConstantDefinition(constant);
//		}
//
//		for (InstructionNode def : transformationInfo.getDefinitionConversations()) {
//			if (def instanceof MethodParameterDefinition) {
//				MethodParameterDefinition paramDef = (MethodParameterDefinition) def;
//
//				for (Integer orgLocal : paramDef.getLocalVariableIndex()) {
//					patchFactory.createOptConversation(orgLocal);
//				}
//			} else if (def instanceof CallResultDefinition) {
//				CallResultDefinition callResultDef = (CallResultDefinition) def;
//
//				System.out.println();
//
//				for (Integer local : callResultDef.getLocalVariableIndex()) {
//					patchFactory.createOptConversationFromStack(local, callResultDef.getByteCodeIndex());
//
//				}
////				callResultDef.
//			}
//		}
//
//
//	}
}
