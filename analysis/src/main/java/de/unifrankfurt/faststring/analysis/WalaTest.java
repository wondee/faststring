package de.unifrankfurt.faststring.analysis;

import java.io.IOException;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;

public class WalaTest {

	public static void main(String[] args) throws IOException, WalaException,
			CancelException {

		TargetApplication targetApplication = new TargetApplication("src/main/resources/test.txt");
		
		
		for (IClass clazz : targetApplication.getApplicationClasses()) {

			System.out.println("-- Class: " + clazz.getName());
			for (IMethod m : clazz.getDeclaredMethods()) {
				SubstringAnalyzer analyzer = new SubstringAnalyzer(targetApplication, m);
				analyzer.findCandidates();

			}

		}
	}

}
