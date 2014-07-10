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
import de.unifrankfurt.faststring.analysis.graph.MethodCallInstruction;
import de.unifrankfurt.faststring.analysis.graph.ParameterNode;
import de.unifrankfurt.faststring.analysis.graph.PhiNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.graph.ReturnNode;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.transform.patches.PatchFactory;


public class MethodTransformer {

	private static final Logger LOG = LoggerFactory.getLogger(MethodTransformer.class);

	private MethodData methodData;
	private MethodEditor editor;
	private TransformationInfo transformationInfo;

	public MethodTransformer(MethodData methodData, TransformationInfo transformationInfo) {
		this.methodData = methodData;
		this.transformationInfo = transformationInfo;
		this.editor = new MethodEditor(methodData);
	}

	public void transformMethod() {
		LOG.info("editing: {}", methodData.getName());

		editor.beginPass();

		createConversations();
		
		try {
			new Verifier(methodData).verify();
			editor.applyPatches();
			editor.endPass();

		} catch (FailureException e) {
			throw new IllegalStateException(e);
		}
	}

	private void createConversations() {

		for (Reference ref : transformationInfo.getReferences()) {
			
			InstructionNode definition = ref.getDefinition();
			createConversations(definition, ref);
			
		}

	}

	private void createConversations(InstructionNode instructionNode, Reference ref) {
		if (!instructionNode.isLabel(ref.getLabel())) {
			
			DefinitionConverter converter = new DefinitionConverter(instructionNode.getLabel(), ref.getLabel());
			convert(instructionNode, ref, converter);
		}
		
		
	}

	private void convert(InstructionNode instructionNode, Reference ref, DefinitionConverter converter) {
		Collection<Integer> locals = instructionNode.getLocalVariableIndex(ref.valueNumber());
		if (!locals.isEmpty()) {
			for (Integer local : locals) {
				converter.setLocal(local);
				instructionNode.visit(converter);
				
			}
		} else {
			instructionNode.visit(converter);
		}
	}

	private class DefinitionConverter implements Visitor {
		
		private int local = -1;
		
		private PatchFactory patchFactory;

		
		public DefinitionConverter(TypeLabel from, TypeLabel to) {
			this.patchFactory = new PatchFactory(transformationInfo, editor, from, to);
		}

		public void setLocal(int local) {
			this.local = local;
		}
		
		@Override
		public void visitConstant(ConstantNode node) {
			patchFactory.createConversationAfter(local, node.getByteCodeIndex());
		}

		@Override
		public void visitMethodCall(MethodCallInstruction callResultDef) {
			patchFactory.createConversationAfter(local, callResultDef.getByteCodeIndex());
		}

		@Override
		public void visitParameter(ParameterNode node) {
			patchFactory.createOptConversation(local);
		}

		@Override
		public void visitPhi(PhiNode node) {
			//TODO to be implemented
			
			// phi defs have merged locals additionally to the locals of their uses
			// can those locals which are taken from the uses be removed from the def locals??
		}

		@Override
		public void visitReturn(ReturnNode node) {
			throw new UnsupportedOperationException("a return does not have a definition");
		}

	}

}
