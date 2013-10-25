package de.unifrankfurt.faststring.yabt.export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

import de.unifrankfurt.faststring.yabt.Result;


public class FileExporter implements ExportStrategy{

	private Path path;
	private String prefix;

	private DecimalFormat df = new DecimalFormat("#");

	public FileExporter(String path, String prefix) {
		this.path = Paths.get(path);
		this.prefix = prefix;
	}


	@Override
	public void export(Result result) {

		try {

			for (String name : result.names()) {
				BufferedWriter writer = Files.newBufferedWriter(path, Charset.defaultCharset());
				for (int run = 0; run < result.runs(); run++) {
					for (int m = 0; m < result.measurments(name, run); m++) {
						writer.write(df.format(result.get(name, run, m)));
					}

					writer.write(String.format("%n"));
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
