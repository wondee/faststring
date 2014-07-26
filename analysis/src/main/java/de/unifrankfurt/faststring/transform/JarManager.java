package de.unifrankfurt.faststring.transform;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wala.shrikeBT.Disassembler;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.analysis.Analyzer.FailureException;
import com.ibm.wala.shrikeBT.shrikeCT.ClassInstrumenter;
import com.ibm.wala.shrikeBT.shrikeCT.OfflineInstrumenter;
import com.ibm.wala.shrikeCT.InvalidClassFileException;

import de.unifrankfurt.faststring.analysis.AnalysisResult;

public class JarManager {

	private static final Logger LOG = LoggerFactory.getLogger(JarManager.class);

	private String baseName;
	private Map<String, AnalysisResult> analysisResult;


	public JarManager(String folderName, String jarBaseName, Map<String, AnalysisResult> analysisResult) {

		baseName = folderName + jarBaseName;
		this.analysisResult = analysisResult;

	}



	public JarManager(String jarFile, Map<String, AnalysisResult> analysisResult) {
		int lastDot = jarFile.lastIndexOf('.');

		baseName = jarFile.substring(0, lastDot);

		this.analysisResult = analysisResult;
	}



	public void process() {
		try {
			OfflineInstrumenter instrumenter = new OfflineInstrumenter();
			instrumenter.addInputJar(new File(baseName + ".jar"));
			instrumenter.setOutputJar(new File(baseName + "-opt.jar"));
			iterateClasses(instrumenter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidClassFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void iterateClasses(OfflineInstrumenter instrumenter)
			throws IOException, InvalidClassFileException, FailureException {

		instrumenter.beginTraversal();
		instrumenter.setPassUnmodifiedClasses(true);

		ClassInstrumenter ci;
		while ((ci = instrumenter.nextClass()) != null) {

			for (int i = 0; i < ci.getReader().getMethodCount(); i++) {

				MethodData methodData = ci.visitMethod(i);


				String signature = (ci.getReader().getName()).replace('/', '.') + "." + methodData.getName() + methodData.getSignature();

				AnalysisResult result = analysisResult.get(signature);
				if (result != null) {

					Path outFile = Paths.get("faststring-output/", signature.replace('/', '.'));

					BufferedWriter writer = Files.newBufferedWriter(outFile, Charset.defaultCharset());

					writer.write("--- old code " + System.getProperty("line.separator"));
					new Disassembler(methodData).disassembleTo(writer);

					new MethodTransformer(methodData, new TransformationInfo(result)).transformMethod();

					writer.write("--- new code " + System.getProperty("line.separator"));
					new Disassembler(methodData).disassembleTo(writer);

					writer.flush();
					writer.close();

					instrumenter.outputModifiedClass(ci, ci.emitClass());
				} else {
					LOG.debug("no analysis results found for {}", signature);
				}

			}

		}

		instrumenter.close();
	}


}
