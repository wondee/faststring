package de.unifrankfurt.faststring.transform;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsInstanceOf.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.ibm.wala.shrikeBT.Disassembler;
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

public class TestMethodTransformer extends BaseAnalysisTest{
	
	private static final Logger LOG = LoggerFactory.getLogger(TestMethodTransformer.class);
	
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
		assertEquals(info.getOptLocal(1), store.getVarIndex());
		
		assertEquals(13, data.getInstructions().length);
	}

	@Test
	public void testCallDef() throws Exception {
		final String methodName = "callDef";
		
		TransformationInfo info = analyze(methodName);
//		MethodData data = transform(info);
//		
//		assertThat(data.getInstructions()[0], instanceOf(LoadInstruction.class));
//		assertThat(data.getInstructions()[1], instanceOf(InvokeInstruction.class));
//		assertThat(data.getInstructions()[2], instanceOf(StoreInstruction.class));
//		
//		LoadInstruction load = (LoadInstruction)data.getInstructions()[0];
//		StoreInstruction store = (StoreInstruction) data.getInstructions()[2];
//		
//		assertEquals(1, load.getVarIndex());
//		assertEquals(info.getOptLocal(1), store.getVarIndex());
//		
//		assertEquals(13, data.getInstructions().length);
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

		new MethodTransformer().transformMethod(methodData, info);
		
		if (LOG.isDebugEnabled()) {
			StringWriter writer = new StringWriter();
			new Disassembler(methodData).disassembleTo("  ", writer);
			
			LOG.debug("transformed code: \n{}", writer.toString());
		}
		
		return methodData;
	}
}
