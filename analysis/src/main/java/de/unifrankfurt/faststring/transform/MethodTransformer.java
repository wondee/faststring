package de.unifrankfurt.faststring.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.MethodEditor;
import com.ibm.wala.shrikeBT.analysis.Analyzer.FailureException;
import com.ibm.wala.shrikeBT.analysis.Verifier;

import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.MethodCallInstruction;
import de.unifrankfurt.faststring.analysis.graph.ParameterDefinition;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.transform.TransformationInfo.Constant;
import de.unifrankfurt.faststring.transform.patches.PatchFactory;


public class MethodTransformer {

	private static final Logger LOG = LoggerFactory.getLogger(MethodTransformer.class);
	
	public void transformMethod(MethodData methodData, TransformationInfo transformationInfo) {
		LOG.info("editing: {}", methodData.getName());

		MethodEditor editor = new MethodEditor(methodData);
		editor.beginPass();

		PatchFactory patchFactory = new PatchFactory(transformationInfo, editor);

		createConstants(patchFactory, editor, transformationInfo);

		try {
			new Verifier(methodData).verify();
		} catch (FailureException e) {
			throw new IllegalStateException(e);
		}

		editor.applyPatches();
		editor.endPass();

	}

	private void createConstants(PatchFactory patchFactory, MethodEditor editor, TransformationInfo transformationInfo) {


		for (Constant constant : transformationInfo.getConstants()) {
			patchFactory.createConstantDefinition(constant);
		}

		for (Reference ref : transformationInfo.getDefinitionConversationsToOpt()) {
			
			InstructionNode definition = ref.getDefinition();
			
			if (definition instanceof ParameterDefinition) {
				for (Integer orgLocal : definition.getLocalVariableIndex(ref.valueNumber())) {
					patchFactory.createOptConversation(orgLocal);
				}
			} else if (definition instanceof MethodCallInstruction) {
				MethodCallInstruction callResultDef = (MethodCallInstruction) definition;

				System.out.println("local for call definition: " + callResultDef);

				for (Integer local : callResultDef.getLocalVariableIndex(ref.valueNumber())) {
					patchFactory.createOptConversationFromStack(local, callResultDef.getByteCodeIndex());

				}
//				callResultDef.
			}
		}


	}
}
