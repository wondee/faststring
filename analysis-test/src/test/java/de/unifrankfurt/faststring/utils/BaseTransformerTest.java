package de.unifrankfurt.faststring.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.ibm.wala.shrikeBT.Disassembler;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.shrikeCT.ClassInstrumenter;
import com.ibm.wala.shrikeCT.ClassReader;
import com.ibm.wala.shrikeCT.InvalidClassFileException;

import de.unifrankfurt.faststring.analysis.AnalysisResult;
import de.unifrankfurt.faststring.analysis.MethodAnalyzer;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.transform.MethodTransformer;
import de.unifrankfurt.faststring.transform.TransformationInfo;

public abstract class BaseTransformerTest extends BaseAnalysisTest {

	private static final Logger LOG = LoggerFactory.getLogger(BaseTransformerTest.class);

	private Map<String, Integer> methodMap;

	public abstract String getTestClass();

	@Before
	public void beforeClass() throws InvalidClassFileException {
		if (methodMap == null) {

			ClassInstrumenter ci = getClassInstrumenter(getTestClass());

			ClassReader reader = ci.getReader();
			int count = reader.getMethodCount();

			methodMap = Maps.newHashMapWithExpectedSize(count);

			for (int i = 0; i < count; i++) {
				methodMap.put(reader.getMethodName(i), i);
			}
		}
	}

	protected TransformationInfo analyze(final String methodName) {
		AnalysisResult result = analyzeToResult(methodName);

		return new TransformationInfo(result);
	}

	protected AnalysisResult analyzeToResult(final String methodName) {
		MethodAnalyzer analyzer = new MethodAnalyzer(getIRMethod(getTestClass(),
				methodName), getTypeLabel());
		return analyzer.analyze();
	}

	protected abstract Collection<TypeLabel> getTypeLabel();

	protected MethodData transform(TransformationInfo info) throws InvalidClassFileException, IOException {
		return transform(info, false);
	}

	protected MethodData transform(TransformationInfo info, boolean printBefore) throws InvalidClassFileException, IOException {

		MethodData methodData = getClassInstrumenter(getTestClass()).visitMethod(methodMap.get(info.getMethodName()));

		if (printBefore) {
			LOG.debug("code before transformation: \n{}", printBytecode(methodData));
		}

		new MethodTransformer(methodData, info).transformMethod();

		LOG.debug("transformed code: \n{}", printBytecode(methodData));

		return methodData;
	}

	private String printBytecode(MethodData methodData) throws IOException {
		StringWriter writer = new StringWriter();
		new Disassembler(methodData).disassembleTo("  ", writer);

		return writer.toString();
	}

}