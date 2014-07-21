package de.unifrankfurt.faststring.analysis.classes;

public class ParserTest {

	public String parse(String line) {

		String result = "";

		for (int i = 0; i < line.length(); i+=2) {
			String substring = line.substring(i, i + 2);

			result += substring;
		}

		return result;
	}

}
