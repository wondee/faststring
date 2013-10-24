package de.unifrankfurt.cs.yabt;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;


public class BenchmarkRunner {

	public static final int DEFAULT_WARM_UP_RUNS = 1000000;
	public static final int DEFAULT_MEASURE_RUNS = 200000;

	public static final List<String> NEEDED_JVM_ARGS;

	public static final String JVM_ARG_GC_OUTPUT = "-verbose:gc";
	public static final String JVM_ARG_COMPILATION_OUTPUT = "-XX:+PrintCompilation";

	static {
		NEEDED_JVM_ARGS = Arrays.asList(JVM_ARG_GC_OUTPUT, JVM_ARG_COMPILATION_OUTPUT);
	}

	public static void main(String[] args) {

	}

	public static void start(Class<?> benchmarkClass) {
		List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();

		for (String arg : NEEDED_JVM_ARGS) {
			if (!arguments.contains(arg)) {
				throw new IllegalStateException("the running JVM has to be launched with the following argument: " + arg);
			}
		}


		BenchmarkClass<?> benchmark = new BenchmarkClass<>(benchmarkClass);
		benchmark.runBenchmarkClass(5, DEFAULT_WARM_UP_RUNS, DEFAULT_MEASURE_RUNS);
	}
}
