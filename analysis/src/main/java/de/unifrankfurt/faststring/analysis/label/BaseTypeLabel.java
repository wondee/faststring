package de.unifrankfurt.faststring.analysis.label;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.MethodReference;

import de.unifrankfurt.faststring.analysis.AnalyzedMethod;
import de.unifrankfurt.faststring.analysis.graph.Reference;

public abstract class BaseTypeLabel implements TypeLabel {

	/**
	 * @return all methods that should be replaced by a signature equal equivalent
	 */
	protected abstract Collection<MethodReference> methods();
	
	@Override
	public int check(SSAInstruction ins) {
		
		if (ins != null) {
			if (ins instanceof SSAAbstractInvokeInstruction) {
				SSAAbstractInvokeInstruction invokeIns = (SSAAbstractInvokeInstruction) ins;
				MethodReference target = invokeIns.getDeclaredTarget();
				if (methods().contains(target) && 
						!invokeIns.isStatic() ) {
					return invokeIns.getReceiver();
				}
			}
		
		}
		return -1;
	}
	
	public Collection<Reference> findTypeUses(AnalyzedMethod ir) {
		Set<Reference> references = Sets.newHashSet();
		
		for (int i = 0; i < ir.getInstructions().length; i++) {
			
			SSAInstruction ins = ir.getInstructions()[i];
			
			int receiver = check(ins);
			
			if (receiver > -1) {
				references.add(new Reference(receiver, this));
				
			}
							
		}
		return references;
	}
	
}
