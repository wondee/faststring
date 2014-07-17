package de.unifrankfurt.faststring.analysis.classes;

public class TransformationUseTestClass {

	public void substring() {
		String a = "test";
		a.substring(3);
	}

	public void substringWithPhi(boolean is) {
		String a = "test";
		String b = "test";
		((is)?a : b).substring(3);
	}

	public void substringWithPhiLocal(boolean is) {
		String a = "test";
		String b = "test";
		String c = ((is)?a : b);
		c.substring(3);
	}
	
	public String substringReturned() {
		String a = "test";
		return a.substring(3);
	}
	
	public String substringReturnedWithLocal() {
		String a = "".substring(3);
		return a;
	}
	
	public String substringUsed() {
		return "".substring(3).intern();
	}
	
	public String substringUsedWithLocal() {
		String a = "".substring(3);
		return a.intern();
	}
	
}
