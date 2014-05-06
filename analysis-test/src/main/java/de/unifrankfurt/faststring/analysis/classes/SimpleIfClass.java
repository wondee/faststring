package de.unifrankfurt.faststring.analysis.classes;

public class SimpleIfClass {

	public String simpleIfSubstring(boolean is) {
		String a = "axyz";
		String b = "bcd";
		String c;
		if (is) {
			c = a.substring(4);
		} else {
			c = b.substring(4);
			
			c = c + b.substring(1, 4);
		}
		
		return c;
	}
	
	String field = "";
	
	public String returnOfUsed(String b) {
		String a = "axyz";
		
		field = a.substring(4);
		
		return a;
	}
	
	public String doIf(boolean is) {
		String a = "a";
		
		String b = "b";
		
		return((is) ? a : b).substring(3); 
		
	}
	
}
