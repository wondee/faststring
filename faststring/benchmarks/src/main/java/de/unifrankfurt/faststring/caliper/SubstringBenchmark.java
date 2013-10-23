
package de.unifrankfurt.faststring.caliper;

import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

import de.unifrankfurt.faststrings.core.SubstringString;

public final class SubstringBenchmark {

	@Benchmark
	public String substringOpt(long count) {
		SubstringString result = new SubstringString("abcdefghijklmnopqrstuvwxyz");

		for (int i = 0; i < count; i++) {
			result.substring(5, 16);
		}

		return result.toString();
	}


	@Benchmark
	public String substringNormal(long count) {
		String result = "abcdefghijklmnopqrstuvwxyz";

		for (int i = 0; i < count; i++) {
			result.substring(5, 16);
		}

		return result;
	}


	public static void main(String[] args) {
		CaliperMain.main(SubstringBenchmark.class, new String[] { "-i", "runtime", "-r", "Substring", "-t", "10" });
	}
}
