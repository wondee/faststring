package de.unifrankfurt.faststring.yabt.export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

import de.unifrankfurt.faststring.yabt.Result;

public class FileExporter implements ExportStrategy {

	private String path;
	private String prefix;

	private DecimalFormat df = new DecimalFormat("#");

	public FileExporter(String path, String prefix) {
		this.path = path;
		this.prefix = prefix;
	}

	@Override
	public void export(Result result) {

		try {

			for (String name : result.names()) {
				BufferedWriter writer = Files.newBufferedWriter(
						createFileName(name), Charset.defaultCharset());
				for (int run = 0; run < result.runs(); run++) {
					for (int m = 0; m < result.measurments(name, run); m++) {
						writer.write(df.format(result.get(name, run, m)));
						writer.write(";");
					}

					writer.write(String.format("%n"));
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Path createFileName(String name) {
		try {

			Path dir = Paths.get(path);
			if (!Files.exists(dir)) {
				Files.createDirectory(dir);
			}

			Path file = Paths.get(path, prefix + name + ".csv");
			if (!Files.exists(file)) {
				Files.createFile(file);
			}
			return file;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

}
