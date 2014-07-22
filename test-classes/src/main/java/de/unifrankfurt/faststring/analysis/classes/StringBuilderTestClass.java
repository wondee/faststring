package de.unifrankfurt.faststring.analysis.classes;

public class StringBuilderTestClass {

	public String testSimple() {
		String b = "abc".substring(3);
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(b);
		
		return builder.toString();
	}
	
	public String testLoop() {
		String a = "ababababababa";
		
		String result = "";
		
		for (int i = 0; i < a.length(); i++) {
			result += a.substring(i, i+2);
		}
		
		return result;
	}
	
	public String testLoopBuilder() {
		String a = "ababababababa";
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < a.length(); i++) {
			builder.append(a.substring(i, i + 2));
		}
		
		return builder.toString();
	}
	
}
