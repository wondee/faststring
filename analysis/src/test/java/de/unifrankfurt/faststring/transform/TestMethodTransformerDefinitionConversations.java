package de.unifrankfurt.faststring.transform;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.ibm.wala.shrikeBT.ConstantInstruction;
import com.ibm.wala.shrikeBT.Disassembler;
import com.ibm.wala.shrikeBT.DupInstruction;
import com.ibm.wala.shrikeBT.InvokeInstruction;
import com.ibm.wala.shrikeBT.LoadInstruction;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.StoreInstruction;
import com.ibm.wala.shrikeBT.shrikeCT.ClassInstrumenter;
import com.ibm.wala.shrikeCT.ClassReader;
import com.ibm.wala.shrikeCT.InvalidClassFileException;

import de.unifrankfurt.faststring.analysis.AnalysisResult;
import de.unifrankfurt.faststring.analysis.MethodAnalyzer;
import de.unifrankfurt.faststring.analysis.label.BuiltInTypes;
import de.unifrankfurt.faststring.utils.BaseAnalysisTest;

public class TestMethodTransformerDefinitionConversations extends BaseAnalysisTest {

	private static final Logger LOG = LoggerFactory.getLogger(TestMethodTransformerDefinitionConversations.class);

	private static final String TEST_CLASS = "TransformationTestClass";

	@Test
	public void testParamDef() throws Exception {
		final String methodName = "paramDef";

		TransformationInfo info = analyze(methodName);
		MethodData data = transform(info);

		assertThat(data.getInstructions()[0], instanceOf(LoadInstruction.class));
		assertThat(data.getInstructions()[1], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[2], instanceOf(StoreInstruction.class));

		LoadInstruction load = (LoadInstruction)data.getInstructions()[0];
		StoreInstruction store = (StoreInstruction) data.getInstructions()[2];

		assertEquals(1, load.getVarIndex());
		assertEquals(info.getLocalForLabel(null, BuiltInTypes.SUBSTRING, 1), store.getVarIndex());

		assertEquals(13, data.getInstructions().length);
	}

	@Test
	public void testConstDef() throws Exception {
		final String methodName = "constantDef";

		TransformationInfo info = analyze(methodName);
		MethodData data = transform(info);
		
		assertThat(data.getInstructions()[1], instanceOf(DupInstruction.class));
		assertThat(data.getInstructions()[2], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[3], instanceOf(StoreInstruction.class));

		StoreInstruction store = (StoreInstruction) data.getInstructions()[3];
		
		assertEquals(info.getLocalForLabel(null, BuiltInTypes.SUBSTRING, 1), store.getVarIndex());

	}
	
	@Test
	public void testConstDefWithoutLocal() throws Exception {
		final String methodName = "constantDefWithoutLocal";

		TransformationInfo info = analyze(methodName);
		MethodData data = transform(info);
		
		assertThat(data.getInstructions()[1], instanceOf(InvokeInstruction.class));

		assertEquals(6, data.getInstructions().length);
	}
	
	@Test
	public void testCallDef() throws Exception {
		final String methodName = "callDef";

		TransformationInfo info = analyze(methodName);
		MethodData data = transform(info);

		assertThat(data.getInstructions()[3], instanceOf(DupInstruction.class));
		assertThat(data.getInstructions()[4], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[5], instanceOf(StoreInstruction.class));

		StoreInstruction store = (StoreInstruction) data.getInstructions()[5];

		assertEquals(info.getLocalForLabel(null, BuiltInTypes.SUBSTRING, 1), store.getVarIndex());

	}
	
	@Test
	public void testCallDefWithoutLocals() throws Exception {
		final String methodName = "callDefWithoutLocals";

		TransformationInfo info = analyze(methodName);
		MethodData data = transform(info);

		assertThat(data.getInstructions()[3], instanceOf(InvokeInstruction.class));

	}
	
	@Test
	public void testCallDefToOriginal() throws Exception {
		final String methodName = "callDefToOriginal";

		TransformationInfo info = analyze(methodName);
		MethodData data = transform(info);

		assertThat(data.getInstructions()[1], instanceOf(InvokeInstruction.class));

	}
	
	
	@Test
	@Ignore
	public void testPhiDef() throws Exception {
		final String methodName = "phiDef";

		TransformationInfo info = analyze(methodName);
		MethodData data = transform(info);

		assertThat(data.getInstructions()[11], instanceOf(LoadInstruction.class));
		assertThat(data.getInstructions()[12], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[13], instanceOf(StoreInstruction.class));

		StoreInstruction store = (StoreInstruction) data.getInstructions()[13];

		assertEquals(info.getLocalForLabel(null, BuiltInTypes.SUBSTRING, 1), store.getVarIndex());

	}

	@Test
	@Ignore
	public void testPhiUses() throws Exception {
		final String methodName = "phiUsesLocalFromUse";

		analyze(methodName);
	}

	private TransformationInfo analyze(final String methodName) {
		MethodAnalyzer analyzer = new MethodAnalyzer(getIRMethod(TEST_CLASS, methodName), BuiltInTypes.SUBSTRING);
		AnalysisResult result = analyzer.analyze();

		return new TransformationInfo(result);
	}

	private MethodData transform(TransformationInfo info)
			throws InvalidClassFileException, IOException {
		ClassInstrumenter ci = getClassInstrumenter(TEST_CLASS);


		ClassReader reader = ci.getReader();
		int count = reader.getMethodCount();

		Map<String, Integer> methodMap = Maps.newHashMapWithExpectedSize(count);

		for (int i = 0; i < count; i++) {
			methodMap.put(reader.getMethodName(i), i) ;
		}

		Integer paramDef = methodMap.get(info.getMethodName());

		MethodData methodData = ci.visitMethod(paramDef);

		new MethodTransformer(methodData, info).transformMethod();

		if (LOG.isDebugEnabled()) {
			StringWriter writer = new StringWriter();
			new Disassembler(methodData).disassembleTo("  ", writer);

			LOG.debug("transformed code: \n{}", writer.toString());
		}

		return methodData;
	}
}
