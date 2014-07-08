package de.unifrankfurt.faststring.utils;

public class TestInitializingException extends RuntimeException {

	private static final long serialVersionUID = 8745587724639365256L;

	public TestInitializingException(String msg, Exception e) {
		super(msg, e);
	}

}
