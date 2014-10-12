package de.unifrankfurt.faststring;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;

public class TestBenchmarks {


	@Test
	public void testRun() throws Exception {
		testClass(XalanBenchmarks.class);
		testClass(ExampleBenchmark.class);
	}

	private void testClass(Class<?> toTest)	throws
		IllegalAccessException, InvocationTargetException, IllegalArgumentException, InstantiationException {
		for (Method m : toTest.getMethods()) {
			if (m.isAnnotationPresent(Benchmark.class)) {
				System.out.println("invoking: " + m.getName());
				m.invoke(toTest.newInstance());
			}
		}
	}
}
