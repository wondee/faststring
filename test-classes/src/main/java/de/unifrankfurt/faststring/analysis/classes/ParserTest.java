package de.unifrankfurt.faststring.analysis.classes;

public class ParserTest {

	public static void main(String[] args) {
		System.out.println(new ParserTest().parse("abababababab"));
	}
	
	public String parse(String line) {
		
		String result = "";
		
		for (int i = 0; i < 8; i+=2) {
			String substring = line.substring(i , i + 2);
			
			result += substring;
		}
		
		return result;
	}
	
}
