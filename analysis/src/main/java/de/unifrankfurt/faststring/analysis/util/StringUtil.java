package de.unifrankfurt.faststring.analysis.util;

import java.util.Collection;
import java.util.Map;

public class StringUtil {
	public static final String LINEBREAK = System.getProperty("line.separator");
	
	public static String toStringMap(Map<?, ?> map) {
		return toStringWithLineBreak(map.entrySet());
	}
	
	public static <E> String toStringWithLineBreak(Collection<E> coll) {
		StringBuilder builder = new StringBuilder(StringUtil.LINEBREAK);
		
		for (E e : coll) {
			builder
				.append("  ")
				.append(e.toString())
				.append(StringUtil.LINEBREAK);
		}
		
		return builder.toString();
	}
}
