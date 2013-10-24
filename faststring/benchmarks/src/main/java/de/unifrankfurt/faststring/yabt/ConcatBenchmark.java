package de.unifrankfurt.faststring.yabt;

import static de.unifrankfurt.faststring.perifidix.Constants.STRING_10;
import de.unifrankfurt.cs.yabt.BeforeEveryCall;
import de.unifrankfurt.cs.yabt.Benchmark;
import de.unifrankfurt.cs.yabt.BenchmarkRunner;

public final class ConcatBenchmark {

	String base;
	String prefix;
	StringBuilder base_;

	@BeforeEveryCall
	public void init() {
		base = STRING_10;
		base_ = new StringBuilder(STRING_10);
		prefix = STRING_10;
	}

	@Benchmark
	public String concatNormal() {
		return base + prefix;
	}

	@Benchmark
	public StringBuilder concatOpt() {
		return base_.append(STRING_10);
	}

	public static void main(String[] args) {
		BenchmarkRunner.start(ConcatBenchmark.class);
	}

}
