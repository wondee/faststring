package de.unifrankfurt.faststring.yabt;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.unifrankfurt.faststring.yabt.export.ExportStrategy;
import de.unifrankfurt.faststring.yabt.export.FileExporter;
import de.unifrankfurt.faststring.yabt.export.PrintStreamExporter;


public class BenchmarkRunner {

	public static final int DEFAULT_RUNS = 5;
	public static final int DEFAULT_INIT_RUNS = 5;
	public static final int DEFAULT_WARM_UP_RUNS = 500000;
	public static final int DEFAULT_MEASURE_RUNS = 20000;

	public static final List<String> NEEDED_JVM_ARGS;

	public static final String JVM_ARG_COMPILATION_OUTPUT = "-XX:+PrintCompilation";

	static {
		NEEDED_JVM_ARGS = Arrays.asList(JVM_ARG_COMPILATION_OUTPUT);
	}

	public static void main(String[] args) {

	}

	public static void start(Class<?> benchmarkClass) {
		start(benchmarkClass, DEFAULT_RUNS, DEFAULT_WARM_UP_RUNS, DEFAULT_INIT_RUNS, DEFAULT_MEASURE_RUNS);
	}

	public static void start(Class<?> benchmarkClass, int runs, int warmUps, int init, int measure) {
		checkJVMSettings();
		createBenchmark(benchmarkClass, runs, warmUps, init, measure, Arrays.asList(new PrintStreamExporter(), new FileExporter("out", benchmarkClass.getSimpleName())));
	}

	private static <T> void createBenchmark(Class<T> benchmarkClass, int runs, int warmUps, int init, int measure, Collection<? extends ExportStrategy> exporters) {

		Experiment<T> benchmark = new Experiment<>(benchmarkClass);

		Result result = benchmark.runBenchmarkClass(runs, warmUps, measure, init);


		for (ExportStrategy exporter : exporters) {
			exporter.export(result);
		}

	}

	private static void checkJVMSettings() {
		List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();

		for (String arg : NEEDED_JVM_ARGS) {
			if (!arguments.contains(arg)) {
				System.err.println("the running JVM has to be launched with the following argument: " + arg);
			}
		}
	}
}
