package de.unifrankfurt.faststring.analysis;

import java.io.IOException;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;

import de.unifrankfurt.faststring.analysis.util.TestUtilities;

public class WalaTest {

	public static void main(String[] args) throws IOException, WalaException,
			CancelException {

		TargetApplication targetApplication = TestUtilities.loadTestClasses();
		
		
		for (IClass clazz : targetApplication.getApplicationClasses()) {

			System.out.println("-- Class: " + clazz.getName());
			for (IMethod m : clazz.getDeclaredMethods()) {
				SubstringAnalyzer analyzer = new SubstringAnalyzer(targetApplication, m);
				analyzer.findCandidates();

			}

		}
	}

}
