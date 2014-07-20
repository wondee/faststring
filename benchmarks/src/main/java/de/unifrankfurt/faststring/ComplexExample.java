package de.unifrankfurt.faststring;

import de.unifrankfurt.faststring.core.StringListBuilder;
import de.unifrankfurt.faststring.core.SubstringString;
import de.unifrankfurt.yabt.annotation.Benchmark;
import de.unifrankfurt.yabt.annotation.BenchmarkConfig;

@BenchmarkConfig(name="complex")
public class ComplexExample {

	String line = "abababababababababababababababababababababababababababababababababababababababababababababababababababababababab";

	@Benchmark
	public String complexNormal() {
		String result = "";

		for (int i = 0; i < line.length(); i+=2) {
			result += line.substring(i, i + 1);
		}

		return result;
	}

	@Benchmark
	public String complexOpt() {
		SubstringString lineOpt = new SubstringString(line);
		StringListBuilder builder = new StringListBuilder();

		for (int i = 0; i < line.length(); i+=2) {
			builder.append(lineOpt.substring(i, i + 1));
		}

		return builder.toString();
	}

}
