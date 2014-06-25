package de.unifrankfurt.faststring.analysis.label;

import de.unifrankfurt.faststring.analysis.LabelAnalyzer;

public final class BuiltInTypes {

	public static final TypeLabel SUBSTRING = SubstringStringType.INSTANCE;
	
	public static final LabelAnalyzer SUBSTRING_ANALYZER = new LabelAnalyzer(SUBSTRING);
	
}
