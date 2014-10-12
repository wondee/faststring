package de.unifrankfurt.faststring.external;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.ShrikeClass;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.shrikeCT.ClassInstrumenter;
import com.ibm.wala.shrikeCT.ClassReader;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.util.WalaException;

import de.unifrankfurt.faststring.analysis.AnalysisResult;
import de.unifrankfurt.faststring.analysis.AnalyzedMethod;
import de.unifrankfurt.faststring.analysis.MethodAnalyzer;
import de.unifrankfurt.faststring.analysis.TargetApplication;
import de.unifrankfurt.faststring.analysis.test.util.PrintTestIRs;
import de.unifrankfurt.faststring.analysis.test.util.TestUtilities;
import de.unifrankfurt.faststring.analysis.util.StringUtil;
import de.unifrankfurt.faststring.transform.MethodTransformer;
import de.unifrankfurt.faststring.transform.TransformationInfo;

public class TestExternalJars {

	private static final Logger LOG = LoggerFactory.getLogger(TestExternalJars.class);

	@Test
	public void testLockObject() throws Exception {
		assertNotNull(createMethodFor("ConcurrentLockSet", "lockObject", "derby.jar"));
	}

	@Test
	public void testHelpFormatter() throws Exception {
		AnalyzedMethod method = createMethodFor("HelpFormatter", "renderWrappedText", "cli.jar");

		assertNotNull(method);

		AnalysisResult analyze = analyze(method);
		transform(method, analyze);

	}

	@Test
	public void testLexer() throws Exception {
		AnalyzedMethod m = createMethodFor("Lexer",
				"org.apache.xpath.compiler.Lexer.tokenize(Ljava/lang/String;Ljava/util/Vector;)V",
				"xalan.jar");

		AnalysisResult analyze = analyze(m);
		transform(m, analyze);

	}


	private AnalyzedMethod createMethodFor(String className, String methodName, String jarName) {
		TargetApplication application = TestUtilities.loadTestJar("target/" + jarName);
		for (IClass cl : application.getClassHierachy()) {
			if (cl.getName().toString().endsWith(className)) {
				LOG.info("found class {}", className);
				for (IMethod m : cl.getAllMethods()) {
					if (m.getName().toString().equals(methodName) || m.getSignature().equals(methodName)) {
						try {
							PrintTestIRs.printToPDF("target", application.getClassHierachy(), application.findIRForMethod(m));
						} catch (WalaException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						LOG.info("found method {}; {}", m.getSignature());
						return application.findIRMethodForMethod(m);

					}
				}
			}
		}

		throw new IllegalStateException(String.format("no method found %s %s", className, methodName));
	}

	private void transform(AnalyzedMethod method, AnalysisResult analyze) throws InvalidClassFileException, IOException {
		ShrikeClass cl = method.getDeclaringClass();
		ClassInstrumenter ci = new ClassInstrumenter(cl.getName().toString(), cl.getReader());

		ClassReader reader = ci.getReader();
		int count = reader.getMethodCount();

		for (int i = 0; i < count; i++) {
			MethodData data = ci.visitMethod(i);

			String signature = (ci.getReader().getName()).replace('/', '.') + "." + data.getName() + data.getSignature();

			if (method.getSignature().equals(signature)) {
				System.out.println(TestUtilities.toStringBytecode(data));
				new MethodTransformer(data, new TransformationInfo(analyze), null).transformMethod();
				System.out.println(TestUtilities.toStringBytecode(data));
			}
		}
	}

	private AnalysisResult analyze(AnalyzedMethod method) {
		MethodAnalyzer methodAnalyzer = new MethodAnalyzer(method, TestUtilities.loadTestLabels("SubstringString", "StringListBuilder"));
		AnalysisResult analyze = methodAnalyzer.analyze();

		System.out.println(StringUtil.toStringWithLineBreak(analyze.getRefs()));

		assertFalse(analyze.isEmpty());
		return analyze;
	}

}
