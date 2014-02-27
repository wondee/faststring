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
}
