package de.unifrankfurt.faststring.analysis.classes;

public class MethodAnalyzerTestClass {

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
	
	
	public String[] phiSimple(boolean is) {
		String a = "";
		String b = a;
		
		if (is) {
			b = "had";
		}
		
		a.substring(4);
		
		return new String[]{b};
	}
	
	public String phiLoop(boolean is) {
		String a = "a";
		
		String b = "b";
		
		while(is) {
			String c = ((is) ? a : b);
			
			b = c.substring(5);
		}
		return b;
		
	}
	
	public String phiLoopAndIf(boolean is) {
		String a = "a";
		
		String b = "b";
		
		String c = (is) ? a : b;
		
		while(is) {
			String d = ((is) ? a : b);
			
			if (is) {
				a = d.substring(3);
			} else {
				b = a.substring(4);
			}
		}
		
		c.substring(9);
		
		return b;
		
	}
	
	public void paramDef(String a) {
		String b = a.substring(4);
		b.substring(3, 6);
	}
	
}
