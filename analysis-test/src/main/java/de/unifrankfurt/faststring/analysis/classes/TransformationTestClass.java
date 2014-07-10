package de.unifrankfurt.faststring.analysis.classes;

public class TransformationTestClass {

	public String callDef() {
		String b = phiDef(true);
		String c = b.substring(3);

		return c;
	}
	
	public String callDefWithoutLocals() {
		String b = phiDef(true).substring(3);
		return b;
	}
	
	public void callDefToOriginal() {
		String b = "".substring(3);
		
		b.intern();
	}
	
	public void paramDef(String a) {
		String b = a.substring(4);
		b.substring(3, 6);
	}
	
	public void constantDef() {
		String a = "test";
		
		a.substring(3);
	}

	public void constantDefWithoutLocal() {
		"test".substring(4);
	}
	
	public String phiDef(boolean is) {
		String a = "test";
		String b = "test2";
		
		String c = ((is) ? a : b);
		
		return c.substring(9);
	}
	
	public String phiUsesLocalFromUse(boolean is, boolean is2) {
		String a = "sdv";
		String b = "asfg";
		
		String c = ((is) ? a : b);
		
		return ((is2) ?	c : b).substring(3);
	}
	
}
