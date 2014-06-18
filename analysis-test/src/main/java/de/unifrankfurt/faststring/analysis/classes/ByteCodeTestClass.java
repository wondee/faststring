package de.unifrankfurt.faststring.analysis.classes;

import de.unifrankfurt.faststring.core.SubstringString;

public class ByteCodeTestClass {

	public String stringFromCallResult() {
		String a = f();
		
		a.substring(5);
		
		return a;
	}
	
	private String f() {
		return "abd";
	}
	
	public String stringFromCallResultOpt() {
		String producedString = f();
		SubstringString producedStringOpt = SubstringString.valueOf(producedString);
		
		producedStringOpt.substring(5);
		
		producedString = producedStringOpt.toString();
		
		return producedString;
	}

	public void stringFromParam(String a) {
		SubstringString aOpt = new SubstringString(a);
		
		SubstringString bOpt = aOpt.substring(5);
		
		String b = bOpt.toString();
		
		stringFromParam(b);
	}
	
	public boolean stringFromContant() {
		String a = "abd";
		a.substring(3);
		
		return a.endsWith("bb");
	}
	
	public boolean stringFromContantOpt() {
		String a = "abd";
		SubstringString aOpt = new SubstringString(a);
		
		aOpt.substring(3);
		
		a = aOpt.toString();
		
		return a.endsWith("bb");
	}
	
}
