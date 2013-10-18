
package de.unifrankfurt.faststring.benchmark;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

import de.unifrankfurt.faststrings.core.ReplaceRegexString;
import de.unifrankfurt.faststrings.core.SubstringString;

public final class ReplaceRegexBenchmark {

	String result = "abcabcefghijklmklmabcabc";
	String regex = "abc";
//	@Param({"a", "cba", "bbbb"})
	String replacement = "aaaaa";

	ReplaceRegexString result_;
	SubstringString replacement_;


	@BeforeExperiment
	public void init() {
		 replacement_ = new SubstringString(replacement);
		 result_ = new ReplaceRegexString(result);
	}

	@Benchmark
	public String replaceOpt() {
		result_.replaceAll(regex, replacement_);
		return result_.toString();
	}

	@Benchmark
	public String replaceNormal() {
		result.replaceAll(regex, replacement);
		return result;
	}

	public static void main(String[] args) {
		CaliperMain.main(ReplaceRegexBenchmark.class, new String[] { "-i", "runtime", "-r", "ReplaceRegex", "-l", "0" });
	}
}
