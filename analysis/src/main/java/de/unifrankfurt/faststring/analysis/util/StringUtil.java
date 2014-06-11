package de.unifrankfurt.faststring.analysis.util;

import java.util.Arrays;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class StringUtil {
	public static final String LINEBREAK = System.getProperty("line.separator");
	
	public static String toStringMap(Map<?, ?> map) {
		return toStringWithLineBreak(map.entrySet());
	}
	
	public static <E> String toStringWithLineBreak(Iterable<E> coll) {
		StringBuilder builder = new StringBuilder(StringUtil.LINEBREAK);
		
		for (E e : coll) {
			builder
				.append("  ")
				.append(String.valueOf(e))
				.append(StringUtil.LINEBREAK);
		}
		
		return builder.toString();
	}

	public static <T> String toStringWithLineBreakAndIndices(T[] ts) {
		
		
		Iterable<String> list =
			Iterables.transform( Arrays.asList(ts), new Function<T, String>() {

				int i = 0;
				
				@Override
				public String apply(T input) {
					return (i++) + " : " + String.valueOf(input);
				}
			});
		
		return toStringWithLineBreak(list);
		
	}
}
