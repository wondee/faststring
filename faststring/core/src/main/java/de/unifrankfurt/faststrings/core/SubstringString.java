package de.unifrankfurt.faststrings.core;


import java.util.Arrays;

public final class SubstringString {

	private char[] string;

	private int start;
	private int end;

	public SubstringString(String value) {
		this(value.toCharArray(), 0, value.length());
	}

	public SubstringString(char[] string, int start, int end) {
		this.string = string;
		this.start = start;
		this.end = end;
	}

	public SubstringString substring(int pos) {
		return substring(pos, end);
	}


	public SubstringString substring(int from, int to) {
		return new SubstringString(string, from + start, start + to);
	}

	// TODO: Does this method get dispatched because it's overwritten??
	public String toString() {
		return String.valueOf(Arrays.copyOfRange(string, start, end));
	}

	char[] charArray() {
		return string;
	}

}
