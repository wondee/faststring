package de.unifrankfurt.faststring.yabt.export;

import java.io.PrintStream;

import org.apache.commons.math.stat.StatUtils;

import de.unifrankfurt.faststring.yabt.Result;

public class PrintStreamExporter implements ExportStrategy {

	private PrintStream out;

	public PrintStreamExporter() {
		this(System.out);
	}

	public PrintStreamExporter(PrintStream out) {
		this.out = out;
	}

	@Override
	public void export(Result result) {
		for (String name : result.names()) {

			printf("--- benchmark: %s", name);
			printf("run\t mean\t\t variance\t\t max");
			for (int run = 0; run < result.runs(); run++) {

				double[] results = result.results(name, run);
				double mean = StatUtils.mean(results);
				double variance = StatUtils.variance(results, mean);
				double max = StatUtils.max(results);

				printf("%d\t %f\t %f\t %f", run, mean, variance, max);

			}
		}

	}

	private void printf(String string, Object...args) {
		out.printf(string, args);
		out.println();
	}
}
