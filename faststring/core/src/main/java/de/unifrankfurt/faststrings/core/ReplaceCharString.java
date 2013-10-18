package de.unifrankfurt.faststrings.core;


/**
 * breaks the semantic by not being immutable
 * <pre>
 * ReplaceString x = new ReplaceString("someString");
 * ReplaceString y = x.replace('o', 'a');
 * assert y == x; // returns true
 * </pre>
 *
 * @author Markus
 *
 */
public final class ReplaceCharString {

	private char[] string;

	public ReplaceCharString(String value) {
		string = value.toCharArray();
	}

	public ReplaceCharString replace(char oldChar, char newChar) {
		for (int i = 0; i < string.length; i++) {
			if (string[i] == oldChar) {
				string[i] = newChar;
			}
		}

		return this;
	}



	public final String toString() {
		return String.valueOf(string);
	}


}
