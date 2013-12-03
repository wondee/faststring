package de.unifrankfurt.faststring;

import static de.unifrankfurt.faststring.Constants.STRING_10;
import de.unifrankfurt.faststring.yabt.Benchmark;
import de.unifrankfurt.faststring.yabt.Init;

public final class ConcatBenchmark {

	String base;
	String prefix_10 = STRING_10;
	StringBuilder base_;

	@Init
	public void init() {
		base = STRING_10;
		base_ = new StringBuilder(STRING_10);
	}

	@Benchmark
	public String concatNormal() {
		return base + prefix_10;
	}

	@Benchmark
	public StringBuilder concatOpt() {
		return base_.append(prefix_10);
	}

}
