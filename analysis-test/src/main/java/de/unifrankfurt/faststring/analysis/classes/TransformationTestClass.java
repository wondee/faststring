package de.unifrankfurt.faststring.analysis.classes;

public class TransformationTestClass {

	public String effectedVars() {
		String b = new MethodAnalyzerTestClass().effectedVars(true, "", 6);

		String c = b.substring(3);

		return c;
	}

	public String callDef() {
		String b = new MethodAnalyzerTestClass().effectedVars(true, "", 6);

		String c = b.substring(3);

		return c;
	}
	
	public void paramDef(String a) {
		String b = a.substring(4);
		b.substring(3, 6);
	}
	
	
	
}
