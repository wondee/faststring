package de.unifrankfurt.faststring.perifidix;

import org.perfidix.annotation.Bench;

//@BenchClass(runs=50)
public class ConcatBenchmark {
	
	
	private static final int RUNS = 100;
	
	private String base;
	private String suffix;
	private StringBuilder baseOpt;
	
	public void init() {
		base = "abc";
		
		baseOpt = new StringBuilder(base);
		suffix = "edf";
	}
	
	@Bench(runs = RUNS, beforeEachRun = "init")
	public final void concatNormal() {
		for (int i = 0; i < 10; i++) {
			base = base + suffix;
		}
//		System.out.println(base);
	}
	
	@Bench(runs = RUNS, beforeEachRun = "init")
	public final void concatOpt() {
		for (int i = 0; i < 10; i++) {
			baseOpt.append(suffix);
		}
//		System.out.println(baseOpt.toString());
	}
}
