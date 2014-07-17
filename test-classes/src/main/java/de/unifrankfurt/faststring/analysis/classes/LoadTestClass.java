package de.unifrankfurt.faststring.analysis.classes;

public class LoadTestClass {

	public void testLoad() {
		String a = "test";

		a.startsWith(a.substring(3));
	}

	public void testStore() {
		String a = "abc".substring(3);
		
		a.intern();
	}
	
	public void test2() {

		String a = "";

		a.startsWith(a);
	}

}
