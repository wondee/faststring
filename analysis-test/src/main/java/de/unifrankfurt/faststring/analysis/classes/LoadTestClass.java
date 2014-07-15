package de.unifrankfurt.faststring.analysis.classes;

public class LoadTestClass {

	public void test() {
		String a = "test";

		a.startsWith(a.substring(3));
	}

	public void test2() {

		String a = "";

		a.startsWith(a);
	}

}
