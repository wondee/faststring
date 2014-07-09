package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.InvokeInstruction;
import com.ibm.wala.shrikeBT.Util;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class ConversationToLabelPatch extends BasePatch {

	final Class<?> originalType;
	final Class<?> optimizedType;
	
	final InvokeInstruction invokeStaticFactory;

	
	public ConversationToLabelPatch(TypeLabel label) {
		
		originalType = label.getOriginalType();
		optimizedType = label.getOptimizedType();
		
		invokeStaticFactory = Util.makeInvoke(optimizedType, label.getCreationMethodName(), new Class<?>[]{originalType});
	}

	@Override
	protected void createInstructions(OutputBuilder w) {
		w.emit(invokeStaticFactory);
	}

}
