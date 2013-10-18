
package de.unifrankfurt.faststring.benchmark;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

import de.unifrankfurt.faststrings.core.ReplaceCharString;

public final class ReplaceCharBenchmark {

	String result = "abcdcba";
	ReplaceCharString result_;

	@BeforeExperiment
	public void init() {
		result_ = new ReplaceCharString(result);
	}

	@Benchmark
	public String replaceOpt() {
		result_.replace('a', 'm');
		return result.toString();
	}


	@Benchmark
	public String replaceNormal() {
		result.replace('a', 'm');
		return result;
	}


	public static void main(String[] args) {
		CaliperMain.main(ReplaceCharBenchmark.class, new String[] { "-i", "runtime", "-r", "ReplaceChar", "-l", "0" });
	}
}
