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
import com.ibm.wala.shrikeBT.analysis.ClassHierarchyStore;
import com.ibm.wala.shrikeBT.shrikeCT.ClassInstrumenter;
import com.ibm.wala.shrikeBT.shrikeCT.OfflineInstrumenter;
import com.ibm.wala.shrikeCT.InvalidClassFileException;

import de.unifrankfurt.faststring.analysis.AnalysisResult;
import de.unifrankfurt.faststring.analysis.util.FileUtil;

/**
 * processes a input jar file for the analysis and tranformation process
 *
 * @author markus
 *
 */
public class JarManager {

	private static final Logger LOG = LoggerFactory.getLogger(JarManager.class);

	private String baseName;
	private Map<String, AnalysisResult> analysisResult;

	private ClassHierarchyStore store;



	public static JarManager createForJar(String folderName, String jarBaseName, Map<String, AnalysisResult> analysisResult) {
		return new JarManager(folderName + jarBaseName, analysisResult, null);
	}

	public static JarManager createForJar(String jarFile, Map<String, AnalysisResult> analysisResult, ClassHierarchyStore store) {
		int lastDot = jarFile.lastIndexOf('.');

		return new JarManager(jarFile.substring(0, lastDot), analysisResult, store);
	}


	private JarManager(String baseName, Map<String, AnalysisResult> analysisResult, ClassHierarchyStore store) {
		this.baseName = baseName;
		this.analysisResult = analysisResult;
		this.store = store;
	}



	public void process() {
		try {
			OfflineInstrumenter instrumenter = new OfflineInstrumenter();
			instrumenter.addInputJar(new File(baseName + ".jar"));
			instrumenter.setOutputJar(new File(baseName + "-opt.jar"));
			iterateClasses(instrumenter);

			instrumenter.close();

		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private void iterateClasses(OfflineInstrumenter instrumenter) throws IOException {

		instrumenter.beginTraversal();
		instrumenter.setPassUnmodifiedClasses(true);

		ClassInstrumenter ci;
		int count = 1;

		while ((ci = instrumenter.nextClass()) != null) {

			for (int i = 0; i < ci.getReader().getMethodCount(); i++) {

				try {
					MethodData methodData = ci.visitMethod(i);

					if (methodData != null) {
						String signature = createSignature(ci, methodData);

						AnalysisResult result = analysisResult.get(signature);
						if (result != null) {
							LOG.info("editing: ({}/{}) {}", count++, analysisResult.size(), methodData.getName());
							try {
								processMethod(methodData, signature, result);

								instrumenter.outputModifiedClass(ci, ci.emitClass());
							} catch(Throwable e) {
								FileUtil.handleError(e, signature);
							}

						}

					} else {
						LOG.error("method data was null");
					}
				} catch (InvalidClassFileException e) {
					LOG.error("could not process method", e);
				}

			}

		}


	}

	private String createSignature(ClassInstrumenter ci, MethodData methodData)
			throws InvalidClassFileException {
		return (ci.getReader().getName()).replace('/', '.') + "." + methodData.getName() + methodData.getSignature();
	}

	private void processMethod(MethodData methodData, String signature,
			AnalysisResult result) throws IOException,
			InvalidClassFileException {

		FileUtil.checkOutputPath("faststring-output/");
		Path outFile = Paths.get("faststring-output/", signature.replace('/', '.'));

		BufferedWriter writer = Files.newBufferedWriter(outFile, Charset.defaultCharset());

		writer.write("--- old code " + System.getProperty("line.separator"));
		new Disassembler(methodData).disassembleTo(writer);

		new MethodTransformer(methodData, new TransformationInfo(result), store).transformMethod();

		writer.write("--- new code " + System.getProperty("line.separator"));
		new Disassembler(methodData).disassembleTo(writer);

		writer.flush();
		writer.close();
	}


}
