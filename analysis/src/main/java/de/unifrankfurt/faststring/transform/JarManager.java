package de.unifrankfurt.faststring.transform;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
//
//	private static final Logger LOG = LoggerFactory.getLogger(JarManager.class);
//
//	private String baseName;
//	private Map<String, AnalysisResult> analysisResult;
//
//
//	public JarManager(String folderName, String jarBaseName, Map<String, AnalysisResult> analysisResult) {
//
//		baseName = folderName + jarBaseName;
//		this.analysisResult = analysisResult;
//
//	}
//
//	public void process() {
//		try {
//			OfflineInstrumenter instrumenter = new OfflineInstrumenter();
//			instrumenter.addInputJar(new File(baseName + ".jar"));
//			instrumenter.setOutputJar(new File(baseName + "-opt.jar"));
//			iterateClasses(instrumenter);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidClassFileException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FailureException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//
//	private void iterateClasses(OfflineInstrumenter instrumenter)
//			throws IOException, InvalidClassFileException, FailureException {
////		final GetInstruction getOut = Util.makeGet(System.class, "out");
////		final Instruction callPrintln = Util.makeInvoke(PrintStream.class, "println", new Class[] { String.class });
//
//		instrumenter.beginTraversal();
//		PrintWriter writer = new PrintWriter(System.out);
//		instrumenter.setPassUnmodifiedClasses(true);
//
//		MethodTransformer transformer = new MethodTransformer();
//
//		ClassInstrumenter ci;
//		while ((ci = instrumenter.nextClass()) != null) {
//
//			for (int i = 0; i < ci.getReader().getMethodCount(); i++) {
//
//
//
//				MethodData methodData = ci.visitMethod(i);
//
//				String signature = ci.getReader().getName().replace('/', '.') + "." + methodData.getName() + methodData.getSignature();
//
//				AnalysisResult result = analysisResult.get(signature);
//				if (result != null) {
//					System.out.println("--- old code");
//					new Disassembler(methodData).disassembleTo(writer);
//					writer.flush();
//
//					transformer.transformMethod(methodData, new TransformationInfo(result));
//
//					System.out.println("--- new code");
//					new Disassembler(methodData).disassembleTo(writer);
//					writer.flush();
//
//
//					instrumenter.outputModifiedClass(ci, ci.emitClass());
//				} else {
//					LOG.debug("no analysis results found for {}", signature);
//				}
//
//			}
//
//		}
//
//		instrumenter.close();
//	}


}
