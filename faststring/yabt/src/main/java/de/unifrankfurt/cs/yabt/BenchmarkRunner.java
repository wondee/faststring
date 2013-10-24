package de.unifrankfurt.cs.yabt;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;


public class BenchmarkRunner {

	public static final int DEFAULT_INIT_RUNS = 5;
	public static final int DEFAULT_WARM_UP_RUNS = 1000000;
	public static final int DEFAULT_MEASURE_RUNS = 500000;

	public static final List<String> NEEDED_JVM_ARGS;

	public static final String JVM_ARG_COMPILATION_OUTPUT = "-XX:+PrintCompilation";

	static {
		NEEDED_JVM_ARGS = Arrays.asList(JVM_ARG_COMPILATION_OUTPUT);
	}

	public static void main(String[] args) {

	}

	public static void start(Class<?> benchmarkClass) {
		checkJVMSettings();
		createBenchmark(benchmarkClass);
	}

	private static <T> void createBenchmark(Class<T> benchmarkClass) {

		Experiment<T> benchmark = new Experiment<>(benchmarkClass);

		benchmark.runBenchmarkClass(5, DEFAULT_WARM_UP_RUNS, DEFAULT_MEASURE_RUNS, DEFAULT_INIT_RUNS );
	}

	private static void checkJVMSettings() {
		List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();

		for (String arg : NEEDED_JVM_ARGS) {
			if (!arguments.contains(arg)) {
				throw new IllegalStateException("the running JVM has to be launched with the following argument: " + arg);
			}
		}
	}
}
