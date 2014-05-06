package de.unifrankfurt.faststring.analysis.wala;

import java.io.IOException;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.config.AnalysisScopeReader;


public class WalaTest {

	
	
	public static void main(String[] args) throws IOException, WalaException {
		AnalysisScope scope = AnalysisScopeReader.readJavaScope("src/main/resources/test.txt", null, WalaTest.class.getClassLoader());
		
		// build a type hierarchy
	    System.out.println("building class hierarchy...");
	    ClassHierarchy cha = ClassHierarchy.make(scope);
	    
	    MethodAnalyzer analyzer = new MethodAnalyzer();
	    
	    for (IClass clazz : cha) {
	    	
	    	if (scope.isApplicationLoader(clazz.getClassLoader()))  {
	    		
	    		System.out.println("-- Class: " + clazz.getName());
	    		for (IMethod m : clazz.getDeclaredMethods()) {
	    			
	    			analyzer.findCandidates(m);
	    			
	    			
	    		}
	    		
	    	}
	    	
	    	
	    }
	}

}

