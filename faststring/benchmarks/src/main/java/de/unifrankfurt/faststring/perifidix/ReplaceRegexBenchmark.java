package de.unifrankfurt.faststring.perifidix;

import static de.unifrankfurt.faststring.perifidix.Constants.ORIG;

import org.perfidix.annotation.Bench;

import de.unifrankfurt.faststrings.core.ReplaceRegexString;
import de.unifrankfurt.faststrings.core.SubstringString;

public class ReplaceRegexBenchmark {
	
	private String result;
	private ReplaceRegexString result_;
	private SubstringString replacement_;
	private String replacement;
	
	@SuppressWarnings("unused")
	private String finalString;

	public void init() {
		result = ORIG;
		result_ = new ReplaceRegexString(ORIG);
		replacement = "Hallo";
		replacement_ = new SubstringString(replacement);
	}
	
	@Bench(runs=100, beforeEachRun="init")
	public final void replaceRegexNormal() {
		finalString = result.replaceAll("abc", replacement);
	}
	
	@Bench(runs=100, beforeEachRun="init")
	public final void replaceRegexOpt() {
		finalString = result_.replaceAll("abc", replacement_);
	}
}
