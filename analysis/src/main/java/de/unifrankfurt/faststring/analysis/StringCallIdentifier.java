package de.unifrankfurt.faststring.analysis;

import java.util.List;

import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.analysis.label.Label;

public class StringCallIdentifier {

	private List<MethodReference> methods;
	private Label label;


	public StringCallIdentifier(Label label) {
		this.label = label;
		methods = label.methods();
		
	}
	
	/**
	 * checks if the given instruction represents a call to a stored method
	 * @param ins the {@link SSAInstruction} to check
	 * @return the value number of the call receiver if the method is contained in the {@link #methods} list, -1 otherwise
	 */
	public int check(SSAInstruction ins) {
		
		if (ins != null) {
			if (ins instanceof SSAAbstractInvokeInstruction) {
				SSAAbstractInvokeInstruction invokeIns = (SSAAbstractInvokeInstruction) ins;
				MethodReference target = invokeIns.getDeclaredTarget();
				if (methods.contains(target) && 
						!invokeIns.isStatic() ) {
					return invokeIns.getReceiver();
				}
			}
		
		}
		return -1;
	}

	public Label label() {
		return label;
	}
	
}
