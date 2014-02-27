package de.unifrankfurt.faststring.analysis;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.BasicValue;

public class StringValue extends BasicValue {

	private int index;

	public StringValue(int var) {
		super(Type.getType(String.class));
		index = var;
	}
	
	
	public int getIndex() {
		return index;
	}
	
	@Override
	public String toString() {
		return "(StringVar: " + index + ")";
	}
}
