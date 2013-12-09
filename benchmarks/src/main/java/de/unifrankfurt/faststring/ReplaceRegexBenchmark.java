package de.unifrankfurt.faststring;


import static de.unifrankfurt.faststring.Constants.REGEX;
import static de.unifrankfurt.faststring.Constants.REPLACEMENT;
import static de.unifrankfurt.faststring.Constants.REPLACEMENT_;
import static de.unifrankfurt.faststring.Constants.REPLACE_BASE_STRING;
import de.unifrankfurt.faststring.yabt.annotation.Benchmark;
import de.unifrankfurt.faststring.yabt.annotation.Init;
import de.unifrankfurt.faststrings.core.ReplaceRegexString;

public class ReplaceRegexBenchmark {

	private String base;
	private ReplaceRegexString base_;

	@Init
	public void init() {
		base = REPLACE_BASE_STRING;
		base_ = new ReplaceRegexString(base);
	}

	@Benchmark
	public String replaceRegexNormal() {
		return base.replaceAll(REGEX, REPLACEMENT);
	}

	@Benchmark
	public String replaceRegexOpt() {
		return base_.replaceAll(REGEX, REPLACEMENT_).toString();
	}


}
