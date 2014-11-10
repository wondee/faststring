package de.unifrankfurt.faststring.analysis.label;

import java.util.Collection;

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
	 * @return the optimized type of the optimization
	 */
	Class<?> getOptimizedType();

	/**
	 * @return the name of the static method with exactly one parameter of the original type to create a new
	 * optimized instance of that original instance
	 */
	String getCreationMethodName();

	/**
	 * @param label
	 * @return <code>true</code> if this label is compatible with the given label, <code>false</code> otherwise
	 */
	boolean compatibleWith(TypeLabel label);

	/**
	 * @return the class object of the original type
	 */
	Class<?> getOriginalType();

	/**
	 * @return the name of the method with no parameters to get a original instance of the optimized instance
	 */
	String getToOriginalMethodName();

	/**
	 * @param target
	 * @return the type in internal jvm notation of the return type of the given method
	 */
	String getReturnType(MethodReference target);

	/**
	 *
	 * @param target
	 * @return
	 */
	String getParams(MethodReference target);


}