package de.unifrankfurt.faststring.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wala.shrikeBT.IInvokeInstruction.Dispatch;
import com.ibm.wala.shrikeBT.InvokeInstruction;
import com.ibm.wala.shrikeBT.LoadInstruction;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.MethodEditor;
import com.ibm.wala.shrikeBT.MethodEditor.Output;
import com.ibm.wala.shrikeBT.MethodEditor.Patch;
import com.ibm.wala.shrikeBT.Util;
import com.ibm.wala.shrikeBT.analysis.Analyzer.FailureException;
import com.ibm.wala.shrikeBT.analysis.Verifier;

import de.unifrankfurt.faststring.analysis.graph.ConstantNode;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode.Visitor;
import de.unifrankfurt.faststring.analysis.graph.MethodCallNode;
import de.unifrankfurt.faststring.analysis.graph.ParameterNode;
import de.unifrankfurt.faststring.analysis.graph.PhiNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.graph.ReturnNode;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.transform.patches.ConversationPatchFactory;


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

			createDefinitionConversations(ref.getDefinition(), ref);

			for (InstructionNode use : ref.getUses()) {
				createUseOptimization(ref, use);
//				createUseConversations(ref, use);
			}

		}

	}
	private void createUseOptimization(Reference ref, InstructionNode use) {
		if (use.getLabel()!= null) {
			use.visit(new Optimizer(use.getLabel(), ref.valueNumber()));
		}
	}

	private void createDefinitionConversations(InstructionNode instructionNode, Reference ref) {
		if (!instructionNode.isLabel(ref.getLabel())) {
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

	private class Optimizer extends Visitor {

		private int v;
		private TypeLabel label;

		private final String optimizedType;
		
		public Optimizer(TypeLabel label, int v) {
			this.v = v;
			this.label = label;
			optimizedType = Util.makeType(label.getOptimizedType());
		}

		@Override
		public void visitMethodCall(MethodCallNode node) {

			if (node.isReceiver(v)) {
				
				
				for (int local : node.getLocals(v)) {
					Integer loadIndex = node.getLoad(local);
					if (loadIndex != null) {
						final int optLocal = transformationInfo.getLocalForLabel(null, label, local);
						
						editor.replaceWith(loadIndex, new Patch() {
							@Override
							public void emitTo(Output w) {
								w.emit(LoadInstruction.make(optimizedType, optLocal));
								
							}
						});
					}
				}
				
				
				int bcIndex = node.getByteCodeIndex();

				final InvokeInstruction invoke = (InvokeInstruction) editor.getInstructions()[bcIndex];

				
				final InvokeInstruction invokeOpt = InvokeInstruction.make(invoke.getMethodSignature(),
						optimizedType, invoke.getMethodName(), Dispatch.VIRTUAL);

				editor.replaceWith(node.getByteCodeIndex(), new Patch() {
					@Override
					public void emitTo(Output w) {
						w.emit(invokeOpt);
					}
				});

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

}
