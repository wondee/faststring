package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.MethodEditor;
import com.ibm.wala.shrikeBT.MethodEditor.Patch;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.transform.TransformationInfo;


public class ConversationPatchFactory {

	private MethodEditor editor;
	private TransformationInfo info;
	private TypeLabel from;
	private TypeLabel to;


	public ConversationPatchFactory(TransformationInfo transformationInfo, MethodEditor editor, TypeLabel from, TypeLabel to) {
		this.info = transformationInfo;
		this.editor = editor;
		this.from = from;
		this.to = to;
	}


	public void createConversationAtStart(int local) {
		editor.insertAtStart(new LoadFromLocalConversationPatch(to, local, info.getLocalForLabel(from, to, local)));
	}

	public void createConversationAfter(int local, int bcIndex) {
		Patch patch = createPatch(local);

		editor.insertAfter(bcIndex, patch);
	}


	private ConversationToLabelPatch createPatch(int local) {
		return (local != -1) ?
				new OnStackConversationPatch(to, info.getLocalForLabel(from, to, local)) :
				new ConversationToLabelPatch(to);
	}

	public void createConversationBefore(int bcIndex) {
		createConversationBefore(-1, bcIndex);
	}


	public void createConversationAfter(int bcIndex) {
		createConversationAfter(-1, bcIndex);
	}


	public void createConversationBefore(int local, int byteCodeIndex) {
		editor.insertBefore(byteCodeIndex, createPatch(local));

	}

}
