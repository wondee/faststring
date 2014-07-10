package de.unifrankfurt.faststring.analysis.classes;

public class ParserTest {

	public static void main(String[] args) {
		new ParserTest().parse("abababababab");
	}
	
	public String parse(String line) {
		
		String result = "";
		
		for (int i = 0; i < 10; i+=2) {
			String substring = line.substring(i , 2);
			
			
			result += substring;
		}
		
		return result;
	}
	
}
