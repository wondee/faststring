
package de.unifrankfurt.faststring.caliper;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;
import com.google.caliper.runner.Running.BeforeExperimentMethods;

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
		System.out.println("init()");
//		replacement_ = new SubstringString(replacement);
//		result_ = new ReplaceRegexString(result);
	}

	@Benchmark
	public String replaceOpt(long i) {
		System.out.println("replaceOpt() " + i);
//		result_.replaceAll(regex, replacement_);
//		return result_.toString();
		return null;
	}

	@Benchmark
	public String replaceNormal(long i) {
		System.out.println("replaceNormal() " + i);
//		result.replaceAll(regex, replacement);
//		return result;
		return null;
	}

	public static void main(String[] args) {
		CaliperMain.main(ReplaceRegexBenchmark.class, new String[] { "-i", "runtime", "-r", "ReplaceRegex", "-l", "0", "-v" });
	}
}
