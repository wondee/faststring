package de.unifrankfurt.faststring.runner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import de.unifrankfurt.faststring.ComplexExample;
import de.unifrankfurt.yabt.BenchmarkSuite;
import de.unifrankfurt.yabt.export.FileExporter;
import de.unifrankfurt.yabt.export.PrintStreamExporter;

public class Main {

	@Parameter(names = "-f", description = "export results to file")
	private boolean file;

	public static void main(String[] args) {

		Main main = new Main();

		new JCommander(main, args);

		main.run();


	}

	private void run() {
		BenchmarkSuite suite = new BenchmarkSuite(
	//			SubstringBenchmark.class,
	//			ConcatBenchmark.class,
	//			ReplaceCharBenchmark.class,
	//			ReplaceRegexBenchmark.class,

				ComplexExample.class

//				ParserBenchmark.class
				);


		if (file)
			suite.addExporter(new FileExporter());

		suite.addExporter(new PrintStreamExporter());

		suite.run();
	}

}
