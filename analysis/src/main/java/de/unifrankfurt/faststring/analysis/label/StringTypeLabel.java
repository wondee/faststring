package de.unifrankfurt.faststring.analysis.label;

import java.util.List;
import java.util.Set;

import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.analysis.IRMethod;
import de.unifrankfurt.faststring.analysis.graph.StringReference;

public interface StringTypeLabel {

	Set<MethodReference> methods();

	boolean canBeUsedAsParamFor(MethodReference method,	int index);

	boolean canBeUsedAsReceiverFor(MethodReference method);

	boolean canBeDefinedAsResultOf(MethodReference method);

	/**
	 * checks if the given instruction represents a call to a stored method
	 * @param ins the {@link SSAInstruction} to check
	 * @return the value number of the call receiver if the method is contained in the {@link #methods} list, -1 otherwise
	 */
	int check(SSAInstruction ins);

	List<StringReference> findStringUses(IRMethod ir);

}