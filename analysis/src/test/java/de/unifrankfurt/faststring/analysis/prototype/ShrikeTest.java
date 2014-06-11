package de.unifrankfurt.faststring.analysis.prototype;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;

import de.unifrankfurt.faststring.analysis.SubstringAnalyzer;
import de.unifrankfurt.faststring.analysis.TargetApplication;
import de.unifrankfurt.faststring.analysis.graph.StringReference;
import de.unifrankfurt.faststring.analysis.util.StringUtil;
import de.unifrankfurt.faststring.analysis.util.TestUtilities;
import de.unifrankfurt.faststring.transform.JarManager;

public class ShrikeTest {

	/*
	 * Verifier 									// for typechecking
	 * ClassInstrumenter.createEmptyMethodData		// for creating new methods
	 * 
	 * 
	 */
	
	public static void main(String[] args) throws Exception {
		
		TargetApplication targetApplication = TestUtilities.loadTestJar();
		
		Map<String, Set<StringReference>> analysisResult = Maps.newHashMap();
		
		for (IClass clazz : targetApplication.getApplicationClasses()) {

			System.out.println("-- Class: " + clazz.getName());
			for (IMethod m : clazz.getDeclaredMethods()) {
				SubstringAnalyzer analyzer = new SubstringAnalyzer(targetApplication, m);
				
				Set<StringReference> candidates = analyzer.findCandidates();
				
				if (!candidates.isEmpty()) {
					analysisResult.put(m.getSignature(), candidates);
				}

			}

		}
		
		System.out.println("the analysis results are:");
		System.out.println(StringUtil.toStringMap(analysisResult));
		
		new JarManager("../analysis-test/target/", "test", analysisResult).process();
		
	}
	
	
}
