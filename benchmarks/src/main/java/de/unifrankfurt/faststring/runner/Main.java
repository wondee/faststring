package de.unifrankfurt.faststring.runner;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import de.unifrankfurt.faststring.ConcatBenchmark;
import de.unifrankfurt.faststring.ReplaceCharBenchmark;
import de.unifrankfurt.faststring.ReplaceRegexBenchmark;
import de.unifrankfurt.faststring.SubstringBenchmark;
import de.unifrankfurt.faststring.yabt.BenchmarkRunner;

public class Main {

	private static final Map<String, Class<?>> BENCHMARKS;

	static {
		BENCHMARKS = ImmutableMap.<String, Class<?>> builder()
				.put("substring", SubstringBenchmark.class)
				.put("concat", ConcatBenchmark.class)
				.put("replaceChar", ReplaceCharBenchmark.class)
				.put("replaceRegex", ReplaceRegexBenchmark.class)
				.build();

	}

	public static void main(String[] args) {
		if (args.length != 1) {
			printUsage();
		} else {
			Class<?> benchmarkToRun = BENCHMARKS.get(args[0]);

			if (benchmarkToRun != null) {
//				BenchmarkRunner.start(benchmarkToRun);
			} else {
				printUsage();
				throw new IllegalArgumentException(args[0] + " not found as a benchmark");
			}
		}
	}

	private static void printUsage() {
		System.out.println("only one param is allowed. Give the name of the benchmark to be runned.");
		System.out.println("available benchmarks: ");
		System.out.println(BENCHMARKS.keySet());
	}
}
