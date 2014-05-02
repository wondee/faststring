package de.unifrankfurt.faststring.analysis.wala;

import java.io.IOException;
import java.util.Iterator;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.graph.traverse.BFSPathFinder;
import com.ibm.wala.util.strings.Atom;


public class WalaTest {

	private static final TypeReference STRING_TYPE = TypeReference.findOrCreate(ClassLoaderReference.Application, "Ljava/lang/String");
	
	
	public static void main(String[] args) throws IOException, WalaException {
		AnalysisScope scope = AnalysisScopeReader.readJavaScope("src/main/resources/test.txt", null, WalaTest.class.getClassLoader());
		
		// build a type hierarchy
	    System.out.println("building class hierarchy...");
	    ClassHierarchy cha = ClassHierarchy.make(scope);
	    
	    AnalysisCache cache = new AnalysisCache();
	    AnalysisOptions options = new AnalysisOptions();
	    
	    for (IClass clazz : cha) {
	    	
	    	if (scope.isApplicationLoader(clazz.getClassLoader()))  {
	    		
	    		System.out.println("-- Class: " + clazz.getName());
	    		for (IMethod m : clazz.getDeclaredMethods()) {
//	    			List<Integer> substringReceiver = Lists.newLinkedList();
	    			
	    			System.out.println("--- Method: " + m.getName());
	    			
	    			// Build the IR from cache.
	    			IR ir = cache.getSSACache().findOrCreateIR(m, Everywhere.EVERYWHERE, options.getSSAOptions());
	    			DefUse defUse = cache.getSSACache().findOrCreateDU(m, Everywhere.EVERYWHERE, options.getSSAOptions()) ;
	    			SSACFG controlFlowGraph = ir.getControlFlowGraph();
	    			
	    			
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
//									System.out.println("instruction: " + ins);
																		
									// get all uses of the receiver
									Iterator<SSAInstruction> uses = defUse.getUses(receiver);
									
									System.out.println("uses for " + receiver + ": ");
									while (uses.hasNext()) {
										
										SSAInstruction use = uses.next();
										// get the block of the call graph for this use instruction
										ISSABasicBlock targetBlock = ir.getBasicBlockForInstruction(use);
										// get the block of the method call
										ISSABasicBlock source = controlFlowGraph.getBlockForInstruction(i);
										// create a breadth first search to check if the use is located after the method call
										BFSPathFinder<ISSABasicBlock> bfsPathFinder = 
												new BFSPathFinder<ISSABasicBlock>(controlFlowGraph, source, targetBlock);
										
										// print out if this use is located after the method call
										System.out.println("is used afterwards:  " + (bfsPathFinder.find() == null));
										
									}
									
									
									
								}
							}
	    				}
	    					
					}
	    			
//	    			System.out.println("substring receiver: " + substringReceiver);	    			
	    			
//	    			printToPDF(cha, ir, m, clazz);
	    			
	    		}
	    		
	    	}
	    	
	    	
	    }
	}

}

