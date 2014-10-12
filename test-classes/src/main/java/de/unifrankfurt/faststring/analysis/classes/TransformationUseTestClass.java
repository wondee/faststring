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

	public String substringWithBinaryOp() {
		String abd = "";

		int i = 5;
		String a = abd.substring(i, i + 2);
		return a.intern();
	}

	public void startsWith() {
		String a = "abc";

		if (a.startsWith("ab")) {
			a.substring(2);
		}
	}

	public void substringConversation() {
		"Hallo".startsWith("abc".substring(2));
	}

	public void lengthCharAt() {
	    String s = Double.toString(2.56);
	    int len = s.length();

	    s.charAt(len - 2);

	    s.substring(2);
	}

}
