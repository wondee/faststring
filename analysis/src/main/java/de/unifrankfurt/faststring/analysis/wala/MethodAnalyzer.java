package de.unifrankfurt.faststring.analysis.wala;

import static de.unifrankfurt.faststring.analysis.wala.IRUtil.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.strings.Atom;

public class MethodAnalyzer {

	private AnalysisCache cache = new AnalysisCache();
	private AnalysisOptions options = new AnalysisOptions();
	
	public Set<Integer> findCandidates(IMethod m) {
		UseRegister substringReceiver = new UseRegister();
		
		System.out.println("--- Method: " + m.getName());
		
		// Build the IR from cache.
		IR ir = cache.getSSACache().findOrCreateIR(m, Everywhere.EVERYWHERE, options.getSSAOptions());
		
		DefUse defUse = cache.getSSACache().findOrCreateDU(m, Everywhere.EVERYWHERE, options.getSSAOptions()) ;	    			
		
		IRAnalyzer analyzer = new IRAnalyzer(ir);
		
		for (int i = 0; i < ir.getInstructions().length; i++) {
			
			// get the i'th instruction
			SSAInstruction ins = ir.getInstructions()[i];
			// some instructions are null... don't know why
			if (ins != null) {
				// if this is an invocation
				if (ins instanceof SSAAbstractInvokeInstruction) {
					SSAAbstractInvokeInstruction invokeIns = (SSAAbstractInvokeInstruction) ins;
					MethodReference target = invokeIns.getDeclaredTarget();
					TypeReference targetType = target.getDeclaringClass();
					// and the target object is a String and no static call
					if (targetType.equals(STRING_TYPE) && !invokeIns.isStatic()) {
						
						// get the method name and the receivers index
						// TODO: distinguish which method is actually called
						Atom methodName = target.getName();
						int receiver = invokeIns.getReceiver();
						System.out.println(i + ":= methodName: " + methodName + "; called on " + receiver);																		
						
						// first check if there are any uses
						if (defUse.getNumberOfUses(receiver) > 0) {
						
							// get all uses of the receiver
							Iterator<SSAInstruction> uses = defUse.getUses(receiver);
							
							System.out.println("uses for " + receiver + ": ");
							while (uses.hasNext()) {
								
								SSAInstruction use = uses.next();
								String[] localNames = ir.getLocalNames(i, receiver);
								
								boolean usedLater = analyzer.isConnected(i, use);
								
								// print out if this use is located after the method call
								System.out.println(receiver + "(" + Arrays.toString(localNames) + ") is used later:  " + usedLater);
								
								
								
								substringReceiver.add(receiver, usedLater);
								
							}
							
						} else {
							substringReceiver.add(receiver, false);
							
						}
						
					}
				}
			}
				
		}
		// return the SSA variable names for this method
		Set<Integer> candidates = substringReceiver.getCandidates();
		System.out.println("substring receiver: " + candidates);	    			
		
		System.out.println("parameters: " + Arrays.toString(ir.getParameterValueNumbers()));
		
		
		return candidates;
		
	}
	
}
