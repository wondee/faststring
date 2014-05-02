package de.unifrankfurt.faststring.analysis.classes;

public class SingleStringMethod {
//	public String doSubstring() {
//		String a = "Hallo Welt";
//		String b = a.substring(3, 6);
//		
//		return b.substring(3);
//	}
//	
//	public String doReplace() {
//		String a = "Hallo";
//		
//		String b = a.replace('l', 'm');
//		
//		return b;
//	}
	
	public String doIf(boolean is) {
		String a = "a";
		
		String b = "b";
		
		return((is) ? a : b).substring(3); 
		
	}
}
