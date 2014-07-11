package de.unifrankfurt.faststring.analysis.classes;

public class TransformationUseTestClass {

	public void simpleSubstring() {
		String a = "test";
		a.substring(3);
	}
	
	public String simpleSubstringWithReturn() {
		String a = "test";
		return a.substring(3);
	}
	
	public void simpleSubstringWithPhi(boolean is) {
		String a = "test";
		String b = "test";
		((is)?a : b).substring(3);
	}
	
}
