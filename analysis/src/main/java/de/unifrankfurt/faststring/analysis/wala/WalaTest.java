package de.unifrankfurt.faststring.analysis.wala;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.config.AnalysisScopeReader;
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
	    			List<Integer> substringReceiver = Lists.newLinkedList();
	    			
	    			System.out.println("--- Method: " + m.getName());
	    			
	    			// Build the IR and cache it.
	    			IR ir = cache.getSSACache().findOrCreateIR(m, Everywhere.EVERYWHERE, options.getSSAOptions());
	    			DefUse defUse = cache.getSSACache().findOrCreateDU(m, Everywhere.EVERYWHERE, options.getSSAOptions()) ;
	    			int length = ir.getInstructions().length;
	    			
	    			
	    			
	    			for (int v = 0; v < length; v++) {
//						System.out.println("instruction: " + instruction);
	    				
						
//						if (instruction != null) {
//							for (int defIndex = 0; defIndex < instruction.getNumberOfDefs(); defIndex++) {
//								System.out.println(instruction.getDef(defIndex));
//							}
//							
//						}
	    				
	    				
	    				SSAInstruction def = defUse.getDef(v);
	    				
//	    				System.out.println(v + "; " + instruction);
	    				
	    				if (def != null) {
							if (def instanceof SSAAbstractInvokeInstruction) {
								SSAAbstractInvokeInstruction invokeIns = (SSAAbstractInvokeInstruction) def;
								MethodReference target = invokeIns.getDeclaredTarget();
								TypeReference targetType = target.getDeclaringClass();
								System.out.println(targetType);
								if (targetType.equals(STRING_TYPE) && !invokeIns.isStatic()) {
									
									Atom methodName = target.getName();
									int receiver = invokeIns.getReceiver();
									System.out.println("methodName: " + methodName + "; called on " + receiver);
									
									
									substringReceiver.add(receiver);
								}
							}
							
							
//							TypeReference type = inference.getType(v).getTypeReference();
//							if (type != null && type.equals(TypeReference.JavaLangString)) {
//								System.out.println(v + " is a String type!!!");
//								
//								strings.add(v);
//								
//								Iterator<SSAInstruction> uses = defUse.getUses(v);
//								while (uses.hasNext()) {
//									System.out.println("uses: " + uses.next());
//								}
//								
//								
//							} else {
//								System.out.println(v + " is a " + type);
//								
//							}
	    				}
	    					
					}
	    			
	    			System.out.println("substring receiver: " + substringReceiver);
	    			
//	    			System.err.println(ir.toString());
	    			
	    			
//	    			printToPDF(cha, ir, m, clazz);
	    			
	    		}
	    		
	    	}
	    	
	    	
	    }
	}

}

