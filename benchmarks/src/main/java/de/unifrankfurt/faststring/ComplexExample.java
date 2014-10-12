package de.unifrankfurt.faststring;

import de.unifrankfurt.faststring.analysis.classes.ExampleParser;
import de.unifrankfurt.faststring.core.StringListBuilder;
import de.unifrankfurt.faststring.core.SubstringString;
import de.unifrankfurt.yabt.annotation.Benchmark;
import de.unifrankfurt.yabt.annotation.BenchmarkConfig;

@BenchmarkConfig(name="complex", measureInterations=10000 )
public class ComplexExample {

	ExampleParser testee = new ExampleParser();

	String line = "abababababababababababababababababababababababababababababababababababababababababababababababababababababababab";
	SubstringString lineOpt = new SubstringString(line);

	@Benchmark
	public String complexNormal() {
		String result = "";

		for (int i = 0; i < line.length(); i+=2) {
			String substring = line.substring(i, i + 1);
			result += substring;
		}

		return result;
	}

	@Benchmark
	public String complexBuilderOpt() {
		return testee.complexBuilder(line);
	}

	@Benchmark
	public String complexBuilderNormal() {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < line.length(); i+=2) {
			result.append(line.substring(i, i + 1));
		}

		return result.toString();
	}

	@Benchmark
	public String complexOpt() {
		SubstringString lineOpt = new SubstringString(line);
		StringListBuilder builder = new StringListBuilder();

		for (int i = 0; i < lineOpt.length(); i+=2) {
			builder.append(lineOpt.substring(i, i + 1));
		}

		return builder.toString();
	}

}
