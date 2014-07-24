package de.unifrankfurt.faststring.analysis.label;

import java.util.Collection;
import java.util.List;

import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.analysis.AnalyzedMethod;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraphBuilder;
import de.unifrankfurt.faststring.analysis.graph.Reference;
/**
 * defines a label for a type containing optimizable methods. This interface is implemented by
 * concrete optimizable type definition to be used by the analysis.
 * <p>
 * The {@link BaseTypeLabel} class provides some preimplemented methods
 *
 * @see DataFlowGraphBuilder
 * @see BuiltInTypes
 * @see SubstringStringType
 *
 * @author Markus Wondrak
 *
 */
public interface TypeLabel {

	/**
	 * checks whether a labeled reference can be used as a parameter at the given index for
	 * the given {@link MethodReference}
	 *
	 * @param method the method that is called
	 * @param index the parameter index at which the reference is passed to the method
	 * @return {@code true} if a a labeled reference could be passed to that method at the index, {@code false} otherwise
	 */
	boolean canBeUsedAsParamFor(MethodReference method,	int index);

	/**
	 * checks whether the a labeled reference can be used as a receiver of the given method call
	 *
	 * @param method the {@link MethodReference} that is called on that reference
	 * @return {@code true} if the method could be called on that reference, {@code false} otherwise
	 */
	boolean canBeUsedAsReceiverFor(MethodReference method);

	/**
	 * checks whether the given method call could produce a labeled reference
	 *
	 * @param method the {@link MethodReference} that is called to define that reference
	 * @return <code>true</code> if the method could return a labeled reference, <code>false</code> otherwise
	 */
	boolean canBeDefinedAsResultOf(MethodReference method);

	/**
	 * checks if the given instruction represents a call to a stored method
	 * @param ins the {@link SSAInstruction} to check
	 * @return the value number of the call receiver if the method is contained in the {@link #methods} list, -1 otherwise
	 */
	int check(SSAInstruction ins);

	/**
	 * finds all uses of this label matching methods that are found in the given {@link AnalyzedMethod}
	 *
	 * @param ir the {@link AnalyzedMethod} of the method that is checked
	 * @return a collection containing {@link Reference}s that are used in the given method
	 */
	Collection<Reference> findTypeUses(AnalyzedMethod ir);

	/**
	 * creates a {@link ReceiverInfo} object that holds information about the labeled references used as
	 * a receiver for the given method
	 *
	 * @param method the {@link MethodReference} called on the labeled reference
	 * @return the receiver info for that label
	 */
	ReceiverInfo getReceiverUseInfo(MethodReference method);

	/**
	 * checks if the given method returns a labeled reference
	 *
	 * @param method the called {@link MethodReference}
	 * @return <code>true</code> if the method returns a labeled reference, <code>false</code> otherwise
	 */
	boolean canReturnedValueBeLabeled(MethodReference method);

	Class<?> getOptimizedType();

	String getCreationMethodName();

	boolean compatibleWith(TypeLabel label);

	Class<?> getOriginalType();

	String getToOriginalMethodName();

	Class<?> getReturnType(MethodReference target);

	List<Class<?>> getParams(MethodReference target);


}