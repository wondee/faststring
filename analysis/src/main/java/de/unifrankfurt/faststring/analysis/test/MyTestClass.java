package de.unifrankfurt.faststring.analysis.test;

public class MyTestClass {

	public String getMessage(String name) {
		return "Hallo " + name;
	}
	
	public String getMessage(String name1, String name2) {
		return getMessage(name1 + " und " + name2);
	}
	
	public void doSomething() {
		String a = "Hallo Welt!";
		
		String b = a.substring(6);
		
		for(int i = 0; i < 5; i++) {
			System.out.println(b);
		}
	}
}
