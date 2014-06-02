package de.unifrankfurt.faststring.analysis.util;

import java.io.IOException;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.util.WalaException;

import de.unifrankfurt.faststring.analysis.TargetApplication;

public class PrintTestIRs {

	/**
	 * main method to print out all ir for the test classes
	 * @param args no args defined
	 * @throws IOException 
	 * @throws WalaException 
	 */
	public static void main(String[] args) throws IOException, WalaException {
		TargetApplication targetApplication = TestUtilities.loadTestClasses();
		
		for (IClass clazz : targetApplication.getApplicationClasses()) {
			for (IMethod m : clazz.getDeclaredMethods()) {
				PDFUtil.printToPDF(targetApplication.getClassHierachy(), targetApplication.findIRForMethod(m));
			}
		}
	}
}
