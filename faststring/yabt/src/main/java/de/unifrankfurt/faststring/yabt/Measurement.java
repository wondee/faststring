package de.unifrankfurt.faststring.yabt;

public abstract class Measurement {

	private static final int RUNS = 500;
	
	abstract void run();
	
	double measure() {
		long before = System.nanoTime();
		
		for (int i = 0; i < RUNS; i++) {
			run();
		}
		
		return afterMeasurement(System.nanoTime() - before) ;
	}
	
	double afterMeasurement(double value) {
		return value;
	}
}
