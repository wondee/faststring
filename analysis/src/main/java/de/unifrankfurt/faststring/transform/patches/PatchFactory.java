package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.MethodEditor;
import com.ibm.wala.shrikeBT.MethodEditor.Patch;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.transform.TransformationInfo;


public class PatchFactory {

	private MethodEditor editor;
	private TransformationInfo info;
	private TypeLabel from;
	private TypeLabel to;


	public PatchFactory(TransformationInfo transformationInfo, MethodEditor editor, TypeLabel from, TypeLabel to) {
		this.info = transformationInfo;
		this.editor = editor;
		this.from = from;
		this.to = to;
	}


	public void createOptConversation(int local) {
		editor.insertAtStart(new LoadFromLocalConversationPatch(to, local, info.getLocalForLabel(from, to, local)));
	}

	public void createConversationAfter(int local, int bcIndex) {
		Patch patch = (local != -1) ? 
				new OnStackConversationPatch(to, info.getLocalForLabel(from, to, local)) :
				new ConversationToLabelPatch(to);
		
		
		editor.insertAfter(bcIndex, patch);
	}

}
