package de.unifrankfurt.faststring;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import de.unifrankfurt.faststring.analysis.classes.ExampleParser;

public class ExampleBenchmark {

	private static final String TEST_STRING_100 = "abababababababababababababababababababababababababababababababababababababababababababababababababab";

	@Benchmark
	@BenchmarkMode({Mode.AverageTime})
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public void exampleParser() {
		new ExampleParser().parse(TEST_STRING_100);
	}

}
