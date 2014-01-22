package de.unifrankfurt.faststring.analysis;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.BasicValue;

public class StringValue extends BasicValue {

	public StringValue(int var) {
		super(Type.getType(String.class));
	}
	
}
