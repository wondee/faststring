package de.unifrankfurt.cs.yabt;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import org.apache.commons.math.stat.StatUtils;

public class BenchmarkClass<T> {

	private T benchmarkInstance;

	private Class<T> benchmarkClass;

	private Set<Method> beforeCall;
	private Set<Method> benchmarks;

	private Result result;

	public BenchmarkClass(Class<T> benchmarkClass) {
		this.benchmarkClass = benchmarkClass;

		instantiateClass();
		findMethods(benchmarkClass);

	}

	@SuppressWarnings("unchecked")
	private void findMethods(Class<T> benchmarkClass) {
		benchmarks = getAllMethods(benchmarkClass, withAnnotation(Benchmark.class));
		beforeCall = getAllMethods(benchmarkClass, withAnnotation(BeforeEveryCall.class));

	}

	public void runBenchmarkClass(int runs, int warmUpRuns, int measureRuns) {
		instantiateClass();
		result = new Result(runs, measureRuns);

		System.out.println("starting initial warm-up phase");

		for (Method m : benchmarks) {
			System.out.println("starting warm-up phase for benchmark " + m.getName());
			doWarmUp(warmUpRuns, m);
		}

		for (int i = 0; i < runs; i++) {
			System.out.println("Starting run " + i);

			for (Method m : benchmarks) {

				System.out.println("starting measurement of " + m.getName());

				double[] results = measure(measureRuns, m);

				System.out.println("mean: " + StatUtils.mean(results) + ";  " + StatUtils.variance(results));

			}

		}
	}

	private double[] measure(int measureRuns, Method m) {
		double[] results = new double[measureRuns];
		for (int i = 0; i < measureRuns; i++) {
			invokeBeforeCalls();

			results[i] = invokeBenchmark(m);
		}

		return results;

	}

	private void instantiateClass() {
		try {
			benchmarkInstance = benchmarkClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException(
					"given class could not be instantiated "
							+ "via default c'tor: " + benchmarkClass.getName(),	e);
		}
	}

	private void doWarmUp(int runs, Method m) {
		for (int i = 0; i < runs; i++) {
			invokeBeforeCalls();
			invokeBenchmark(m);
		}
	}

	private void invokeBeforeCalls() {
		try {
			for (Method beforeMethod : beforeCall) {
				beforeMethod.invoke(benchmarkInstance);
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private long invokeBenchmark(Method m) {
		try {
			long before = System.nanoTime();

			m.invoke(benchmarkInstance);
			long measurement = System.nanoTime() - before;
			return measurement;
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalStateException(e);
		}

	}
}
