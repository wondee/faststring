package de.unifrankfurt.faststring;

import static de.unifrankfurt.faststring.Constants.NEW_CHAR;
import static de.unifrankfurt.faststring.Constants.OLD_CHAR;
import static de.unifrankfurt.faststring.Constants.REPLACE_BASE_STRING;
import de.unifrankfurt.faststring.core.ReplaceCharString;
import de.unifrankfurt.yabt.annotation.Benchmark;
import de.unifrankfurt.yabt.annotation.BenchmarkConfig;
import de.unifrankfurt.yabt.annotation.Init;

@BenchmarkConfig(name="replaceChar")
public class ReplaceCharBenchmark {

	private String base;
	private ReplaceCharString base_;

	@Init
	public void init() {
		base = REPLACE_BASE_STRING;
		base_ = new ReplaceCharString(REPLACE_BASE_STRING);
	}

	@Benchmark
	public String replaceCharNormal() {
		return base.replace(OLD_CHAR, NEW_CHAR);
	}

	@Benchmark
	public ReplaceCharString replaceCharOpt() {
		return base_.replace(OLD_CHAR, NEW_CHAR);
	}


}
