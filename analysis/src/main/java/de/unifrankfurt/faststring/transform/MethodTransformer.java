package de.unifrankfurt.faststring.transform;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.ibm.wala.shrikeBT.IInstruction;
import com.ibm.wala.shrikeBT.IInvokeInstruction.Dispatch;
import com.ibm.wala.shrikeBT.InvokeInstruction;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.MethodEditor;
import com.ibm.wala.shrikeBT.MethodEditor.Output;
import com.ibm.wala.shrikeBT.MethodEditor.Patch;
import com.ibm.wala.shrikeBT.Util;
import com.ibm.wala.shrikeBT.analysis.Analyzer.FailureException;
import com.ibm.wala.shrikeBT.analysis.Verifier;
import com.ibm.wala.util.strings.StringStuff;

import de.unifrankfurt.faststring.analysis.graph.ConstantNode;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode.Visitor;
import de.unifrankfurt.faststring.analysis.graph.MethodCallInstruction;
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
				createUseConversations(ref, use);
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
			
			if (instructionNode instanceof PhiNode) {
				instructionNode.visit(converter);
			} else {
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
		
	}

	private void createUseConversations(Reference ref, InstructionNode instructionNode) {
		Converter converter = new UseConverter(ref.getLabel(), instructionNode.getLabel());
//		
//		if 
//		
//		instructionNode.visit(visitor);
//		
//		Collection<Integer> locals = instructionNode.getLocals(ref.valueNumber());
//		if (!locals.isEmpty()) {
//			for (Integer local : locals) {
//				converter.setLocal(local);
//				instructionNode.visit(converter);
//				
//			}
//		} else {
//			converter.setLocal(-1);
//			instructionNode.visit(converter);
//		}
		
		
	}

	private class Optimizer extends Visitor {
		
		private int v;
		private TypeLabel label;

		public Optimizer(TypeLabel label, int v) {
			this.v = v;
			this.label = label;
		}
		
		@Override
		public void visitMethodCall(MethodCallInstruction node) {
			
			if (node.isReceiver(v)) {
				int bcIndex = node.getByteCodeIndex();
				
				final InvokeInstruction invoke = (InvokeInstruction) editor.getInstructions()[bcIndex];
				
				System.out.println(invoke.getMethodName());
				System.out.println(invoke.getMethodSignature());
				
				System.out.println(Arrays.toString(Util.getParamsTypes(null, invoke.getMethodSignature())));
				
				
				final InvokeInstruction invokeOpt = InvokeInstruction.make(invoke.getMethodSignature(), 
						Util.makeType(label.getOptimizedType()), invoke.getMethodName(), Dispatch.VIRTUAL);
				
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
	
	private class UseConverter extends Converter {

		public UseConverter(TypeLabel from, TypeLabel to) {
			super(from, to);
		}

		@Override
		public void visitConstant(ConstantNode node) {
			throw new UnsupportedOperationException("a constant can not be a use");
		}

		@Override
		public void visitMethodCall(MethodCallInstruction node) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visitParameter(ParameterNode node) {
			throw new UnsupportedOperationException("a parameter can not be a use");
		}

		@Override
		public void visitPhi(PhiNode node) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visitReturn(ReturnNode node) {
			
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
			
			// first try: just convert on stack. Should be right when no store appears afterwards
			// 
			
			Collection<Integer> defLocals = node.getLocals(node.getDef());
			Collection<Integer> useLocals = Sets.newHashSet();
			for (Integer use : node.getUses()) {
				useLocals.addAll(node.getLocals(use));
			}
			
			if (defLocals.equals(useLocals)) {
				patchFactory.createConversationBefore(node.getByteCodeIndex());
			}
			
		}

		@Override
		public void visitReturn(ReturnNode node) {
			throw new UnsupportedOperationException("a return does not have a definition");
		}

	}

}
