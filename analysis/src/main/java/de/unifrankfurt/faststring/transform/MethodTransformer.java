package de.unifrankfurt.faststring.transform;

import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.MethodEditor;
import com.ibm.wala.shrikeBT.analysis.Analyzer.FailureException;
import com.ibm.wala.shrikeBT.analysis.ClassHierarchyProvider;
import com.ibm.wala.shrikeBT.analysis.Verifier;

import de.unifrankfurt.faststring.analysis.graph.ConstantNode;
import de.unifrankfurt.faststring.analysis.graph.GetNode;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode.Visitor;
import de.unifrankfurt.faststring.analysis.graph.LabelableNode;
import de.unifrankfurt.faststring.analysis.graph.MethodCallNode;
import de.unifrankfurt.faststring.analysis.graph.ParameterNode;
import de.unifrankfurt.faststring.analysis.graph.PhiNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.graph.ReturnNode;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.transform.patches.ConversionPatchFactory;


public class MethodTransformer {

	private static final Logger LOG = LoggerFactory.getLogger(MethodTransformer.class);

	private MethodData methodData;
	MethodEditor editor;
	TransformationInfo transformationInfo;

	private ClassHierarchyProvider store;

	public MethodTransformer(MethodData methodData, TransformationInfo transformationInfo, ClassHierarchyProvider store) {
		this.methodData = methodData;
		this.transformationInfo = transformationInfo;
		this.editor = new MethodEditor(methodData);
		this.store = store;
	}

	public void transformMethod() {
		LOG.info("editing: {}", methodData.getName());

		editor.beginPass();

		Set<LabelableNode> labelableNodes = Sets.newHashSet();
		
		for (Reference ref : transformationInfo.getReferences()) {

			InstructionNode definition = ref.getDefinition();
			
			if (definition instanceof LabelableNode) {
				LabelableNode node = (LabelableNode) definition;
				labelableNodes.add(node);
				createDefinitionConversions(node, ref);
			} else {
				createDefinitionConversions(definition, ref);
			}
			for (InstructionNode use : ref.getUses()) {
				
				if (use instanceof LabelableNode) {
					LabelableNode node = (LabelableNode) use;
					labelableNodes.add(node);
					createUseConversions(ref, node);
				} else {
					createUseConversions(ref, use);
				}
			}

		}
		
		for (LabelableNode node : labelableNodes) {
			createUseOptimization(node);
		}
		
		try {
			Verifier verifier = new Verifier(methodData);

			verifier.setClassHierarchy(store);
			
			verifier.verify();
			editor.applyPatches();
			editor.endPass();

		} catch (FailureException e) {
			throw new IllegalStateException(e);
		}
	}
	


	private void createUseConversions(Reference ref, InstructionNode use) {
		if (ref.getLabel() != null) {
			
			UseConverter converter = new UseConverter(ref.getLabel(), null);
			convertUses(ref, use, converter);
		}
	}

	private void createUseConversions(Reference ref, LabelableNode use) {
		if (use.needsConversionTo(ref)) {
			
			UseConverter converter = new UseConverter(ref.getLabel(), use.getLabel());
			convertUses(ref, use, converter);
		}

	}

	private void convertUses(Reference ref, InstructionNode use, UseConverter converter) {
		Collection<Integer> locals = use.getLocals(ref.valueNumber());

		for (int index : use.getIndicesForV(ref.valueNumber())) {
			if (!locals.isEmpty()) {
				for (Integer local : locals) {
					converter.setLocal(transformationInfo.getLocalForLabel(null, ref.getLabel(), local), index);
					use.visit(converter);

				}
			} else {
				converter.setLocal(-1);
				use.visit(converter);
			}
		}
	}

	private void createUseOptimization(InstructionNode use) {
		if (use instanceof LabelableNode) {
			
			TypeLabel label = ((LabelableNode)use).getLabel();
			if (label != null) {
				use.visit(new Optimizer(new ConversionPatchFactory(transformationInfo, editor, label)));
			}
		}
		
	}

	private void createDefinitionConversions(InstructionNode definition, Reference ref) {
		if (ref.getLabel() != null) {
			Converter converter = new DefinitionConverter(null, ref.getLabel());
			convertDefinitions(definition, ref, converter);
		}
		
	}
	
	private void createDefinitionConversions(LabelableNode instructionNode, Reference ref) {
		if (instructionNode.needsConversionTo(ref)) {
			Converter converter = new DefinitionConverter(instructionNode.getLabel(), ref.getLabel());
			convertDefinitions(instructionNode, ref, converter);
		}

	}

	private void convertDefinitions(InstructionNode instructionNode, Reference ref, Converter converter) {
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

	private abstract class Converter extends Visitor {
		int local = -1;
		ConversionPatchFactory patchFactory;

		public Converter(TypeLabel from, TypeLabel to) {
			this.patchFactory = new ConversionPatchFactory(transformationInfo, editor, from, to);
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

	}

	private class UseConverter extends Converter {

		TypeLabel from;
		private int index;

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
				patchFactory.replaceLoad(orgLocal, node.getLoad(index), from);
			}
			patchFactory.createConversationBefore(node.getByteCodeIndex());
		}

		void setLocal(int local, int index) {
			super.setLocal(local);
			this.index = index;
		}

	}

}
