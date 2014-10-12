package de.unifrankfurt.faststring.core;

import java.util.Arrays;

public final class SubstringString implements CharSequence {

	private final char[] string;

	private final int start;
	private final int end;

//	private final int id;
//
//	static int counter = 0;



	public SubstringString(String value) {
		this(value.toCharArray(), 0, value.length());
	}

	public SubstringString(char[] string, int start, int end) {
		this.string = string;
		this.start = start;
		this.end = end;
//		this.id = counter++;
//		System.out.println(id + ":ctor: " + start + " - " + end);
	}

	public SubstringString substring(int pos) {
		return substring(pos, end);
	}

	public SubstringString substring(int from, int to) {
//		System.out.println(id + ":" + from + " - " + to);
		return new SubstringString(string, from + start, start + to);
	}

	public String toString() {
//		System.out.println(id + ":" + "toString: " + start + " - " + end);
		return String.valueOf(Arrays.copyOfRange(string, start, end));
	}

	char[] charArray() {
		return string;
	}

	@Override
	public int length() {
		return end - start;
	}

	@Override
	public char charAt(int index) {
		return string[start + index];
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return substring(start, end);
	}

	public static SubstringString valueOf(String string) {
		return new SubstringString(string);
	}

}
