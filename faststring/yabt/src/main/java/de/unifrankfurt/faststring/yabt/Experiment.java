package de.unifrankfurt.faststring.yabt;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import org.apache.commons.math.stat.StatUtils;

public final class Experiment<T> {

	private T benchmarkInstance;

	private Class<T> benchmarkClass;

	private Set<Method> inits;
	private Set<Method> benchmarks;

	private Measurement initCallMeasurement = new Measurement() {
		
		@Override
		void run() {
			for (Method m : inits) {
				invokeMethod(m, true);
			}
		}
	};
	
	

	public Experiment(Class<T> benchmarkClass) {
		this.benchmarkClass = benchmarkClass;

		instantiateClass();
		findMethods(benchmarkClass);

	}

	@SuppressWarnings("unchecked")
	private void findMethods(Class<T> benchmarkClass) {
		benchmarks = getAllMethods(benchmarkClass, withAnnotation(Benchmark.class));
		inits = getAllMethods(benchmarkClass, withAnnotation(Init.class));

	}

	public Result runBenchmarkClass(int measureIterations, int warmUpRuns, int measureRuns, int warmUpIterations) {
		instantiateClass();

		System.out.println("starting initial warm-up phase");

		for (int i = 0; i < warmUpIterations; i++) {
			System.out.println("starting warm-up iteration " + i + " of " + warmUpIterations);
			for (Method m : benchmarks) {
				doWarmUp(warmUpRuns, m);
			}
		}
		
		System.out.println("warm up is over starting test runs");

		Result result = new Result(measureIterations);
		
		for (int i = 0; i < measureIterations; i++) {
			System.out.println("Starting test run " + i);
			measureBenchmarks(result, i, measureRuns, 0);
			
		}
		
		if (result.equals(null)) {
			System.out.println("just for avoiding deadcode analysis");
		}
		
		double initTime = measureInitMethods(measureRuns);
		
		System.out.println("estimated init time is " + initTime + " ms");
		
		result = new Result(measureIterations);
		
		for (int i = 0; i < measureIterations; i++) {
			System.out.println("Starting run " + i);
			measureBenchmarks(result, i, measureRuns, initTime);
			
		}

		return result;
	}
	

	private void measureBenchmarks(Result result, int i, int measureRuns, double initTime) {
		
		for (Method m : benchmarks) {
			Measurement measurement = createBenchmarkMeasurement(m, initTime);
			double[] results = measureWithoutGc(measureRuns, measurement);
			
			result.set(i, m.getName(), results);
		}
	}

	private Measurement createBenchmarkMeasurement(final Method m, final double initTime) {
		return new Measurement() {
			@Override
			void run() {
				invokeInits();
				invokeMethod(m);
			}
			@Override
			double afterMeasurement(double value) {
				return value - initTime;
			}
		};
	}

	private double measureInitMethods(int iterations) {
		
		double[] results = measureWithoutGc(iterations, initCallMeasurement);
		return StatUtils.mean(results);
		
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

	private double[] measureWithoutGc(int measureRuns, Measurement measurement) {
		double[] results = new double[measureRuns];
		GcWatcher watcher = new GcWatcher();
		do {
			watcher.reset();
			for (int i = 0; i < measureRuns; i++) {
				results[i] = measurement.measure();
			}
			if (watcher.wasGcActive()) {
				System.err.println("GC run while runnig the benchmark, measurement will be repeated");
			}
			
		} while(watcher.wasGcActive());
		
		return results;
	}
	
	private void doWarmUp(int runs, Method m) {
		for (int i = 0; i < runs; i++) {
			invokeInits();
			invokeMethod(m);
		}
	}

	private void invokeInits() {
		for (Method beforeMethod : inits) {
			invokeMethod(beforeMethod, true);
		}
	}

	
	
	private void invokeMethod(Method m) {
		invokeMethod(m, false);
	}

	/**
	 * use the returned object to avoid deleting of dead code
	 * @param m the method to invoke
	 */
	private void invokeMethod(Method m, boolean nullable) {
		try {

			Object o = m.invoke(benchmarkInstance);
			if (o == null && !nullable) {
				System.err.println("returned value was null. This bias the result. The called method was " + m);
			}
			
		} catch (IllegalAccessException
				| IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalStateException(e);
		}

	}
}
