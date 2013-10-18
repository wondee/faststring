
package de.unifrankfurt.faststring.benchmark;

import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

public final class ConcatBenchmark {

	@Benchmark
	public String concatOpt() {
		StringBuilder result = new StringBuilder("");

//		for (int i = 0; i < count; i++) {
			result.append("ab");
//		}

		return result.toString();
	}


	@Benchmark
	public String concatNormal() {
		String result = "";

//		for (int i = 0; i < count; i++) {
			result += "ab";
//		}

		return result;
	}


	public static void main(String[] args) {
		CaliperMain.main(ConcatBenchmark.class, new String[] { "-i", "runtime", "-r", "Concat" });
	}
}
