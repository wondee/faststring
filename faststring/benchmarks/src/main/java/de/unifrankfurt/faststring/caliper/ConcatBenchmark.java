
package de.unifrankfurt.faststring.caliper;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

public final class ConcatBenchmark {

	@BeforeExperiment
	public void init() {
		System.out.println("init()");
	}

	@Benchmark
	public String concatOpt(int count) {
		System.out.println("concatOpt( " + count + " )");
		StringBuilder result = new StringBuilder("");

		for (int i = 0; i < count; i++) {
			result.append("ab");
		}

		return result.toString();
	}


	@Benchmark
	public String concatNormal(int count) {
		System.out.println("concatNormal( " + count + " )");
		String result = "";

		for (int i = 0; i < count; i++) {
			result += "ab";
		}

		return result;
	}


	public static void main(String[] args) {
		CaliperMain.main(ConcatBenchmark.class, new String[] { "-i", "runtime", "-r", "Concat", "-t", "10" });
	}
}
