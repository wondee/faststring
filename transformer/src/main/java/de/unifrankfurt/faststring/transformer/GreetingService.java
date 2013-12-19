package de.unifrankfurt.faststring.transformer;

public class GreetingService {

	public String getMessage(String name) {
		return "Hallo " + name;
	}
	
	public String getMessage(String name1, String name2) {
		return getMessage(name1 + " und " + name2);
	}
	
}
