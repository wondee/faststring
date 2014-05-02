package de.unifrankfurt.faststring;

import de.unifrankfurt.faststring.core.SubstringString;
import de.unifrankfurt.yabt.annotation.Benchmark;
import de.unifrankfurt.yabt.annotation.BenchmarkConfig;
import de.unifrankfurt.yabt.annotation.Init;

@BenchmarkConfig(name="substring")
public class SubstringBenchmark {

	String base = Constants.STRING_20;
	SubstringString base_;

	int start = 5;
	int end = 10;

	@Init
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


}
