package de.unifrankfurt.faststring.analysis.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;

public final class FileUtil {

	private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

	public static void handleError(Throwable e, String filename) {
		try {
			LOG.error("method is skipped, because of error", e);

			checkOutputPath("faststring-errors/");

			Path errorFile = Paths.get("faststring-errors/", filename.replace('/', '.'));

			StringWriter stringWriter = new StringWriter();
			PrintWriter writer = new PrintWriter(stringWriter);
			e.printStackTrace(writer);

			Iterable<String> stackTrace = Splitter.on(System.getProperty("line.separator")).split(stringWriter.toString());

			Files.write(errorFile, stackTrace, Charset.defaultCharset());

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void checkOutputPath(String path) throws IOException {
		Path outputPath = Paths.get(path);

		if (!Files.exists(outputPath)) {
			Files.createDirectory(outputPath);
		}
	}
}
