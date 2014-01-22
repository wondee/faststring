package de.unifrankfurt.faststring.analysis.test;

public class SingleStringMethod {
	public void doSomething() {
		String a = "Hallo Welt!";
		
		String b = a.substring(6);
		
		for(int i = 0; i < 5; i++) {
			System.out.println(b);
		}
	}
}
