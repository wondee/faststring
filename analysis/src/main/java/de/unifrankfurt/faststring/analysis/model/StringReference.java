package de.unifrankfurt.faststring.analysis.model;

public class StringReference {

	protected int ref;
	protected boolean param = false;

	public StringReference(int ref) {
		super();
		this.ref = ref;
		
	}

	public void setToParam() {
		param = true;
		
	}

	public int valueNumber() {
		return ref;
	}

}