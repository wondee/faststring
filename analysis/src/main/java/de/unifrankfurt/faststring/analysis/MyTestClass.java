package de.unifrankfurt.faststring.analysis;

public class MyTestClass {

	public String getMessage(String name) {
		return "Hallo " + name;
	}
	
	public String getMessage(String name1, String name2) {
		return getMessage(name1 + " und " + name2);
	}
	
	
	public boolean someMethod() {
		int i = 6;
		
		if (i == 5) {
			return true;
		} else 
			return false;
	}
}
