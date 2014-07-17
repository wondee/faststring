package de.unifrankfurt.faststring.transform.patches;

import com.ibm.wala.shrikeBT.InvokeInstruction;
import com.ibm.wala.shrikeBT.Util;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class ConversationToLabelPatch extends BasePatch {

	final Class<?> originalType;
	final Class<?> optimizedType;
	
	final InvokeInstruction invokeStaticFactory;

	
	public ConversationToLabelPatch(TypeLabel from, TypeLabel to) {
		if (to != null) {
			originalType = to.getOriginalType();
			optimizedType = to.getOptimizedType();
			
			invokeStaticFactory = Util.makeInvoke(optimizedType, to.getCreationMethodName(), new Class<?>[]{originalType});			
		} else {
			originalType = from.getOriginalType();
			optimizedType = from.getOptimizedType();
			
			invokeStaticFactory = Util.makeInvoke(optimizedType, from.getToOriginalMethodName(), new Class<?>[0]);
		}
	}

	@Override
	protected void createInstructions(OutputBuilder w) {
		w.emit(invokeStaticFactory);
	}

}
