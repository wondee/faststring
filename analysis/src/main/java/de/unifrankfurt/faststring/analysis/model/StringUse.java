package de.unifrankfurt.faststring.analysis.model;

public class StringUse extends StringReference {
	
	private int insIndex;
	
	public StringUse(int receiver, int insIndex) {
		super(receiver);
		this.insIndex = insIndex;
	}

	
	@Override
	public String toString() {
		return "Use [receiver=" + ref + ", insIndex=" + insIndex + "]";
	}


	public Integer insIndex() {
		return insIndex;
	}
}