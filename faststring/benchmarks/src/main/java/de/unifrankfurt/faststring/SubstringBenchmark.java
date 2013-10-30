package de.unifrankfurt.faststring;

import de.unifrankfurt.faststring.yabt.BeforeEveryCall;
import de.unifrankfurt.faststring.yabt.Benchmark;
import de.unifrankfurt.faststring.yabt.BenchmarkRunner;
import de.unifrankfurt.faststrings.core.SubstringString;

public class SubstringBenchmark {

	String base = Constants.STRING_20;
	SubstringString base_;

	int start = 5;
	int end = 10;

	@BeforeEveryCall
	public void init() {
		base_ = new SubstringString(base);
	}

	@Benchmark
	public String substringNormal() {
		return base.substring(start, end);
	}

	@Benchmark
	public SubstringString substringOpt() {
		return base_.substring(start, end);
	}


	public static void main(String[] args) {
		BenchmarkRunner.start(SubstringBenchmark.class);
	}
}
