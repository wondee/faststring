package de.unifrankfurt.faststring.transform;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import com.ibm.wala.shrikeBT.DupInstruction;
import com.ibm.wala.shrikeBT.InvokeInstruction;
import com.ibm.wala.shrikeBT.LoadInstruction;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.StoreInstruction;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.utils.BaseTransformerTest;

public class TestMethodTransformerDefinitionConversations extends BaseTransformerTest {

	private static final String TEST_CLASS = "TransformationDefinitionTestClass";	
	
	public String getTestClass() {
		return TEST_CLASS;
	}

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
		assertEquals(info.getLocalForLabel(null, SUBSTRING, 1), store.getVarIndex());

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

		assertEquals(info.getLocalForLabel(null, SUBSTRING, 1), store.getVarIndex());

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

		assertEquals(info.getLocalForLabel(null, SUBSTRING, 1), store.getVarIndex());

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
	public void testPhiDef() throws Exception {
		final String methodName = "phiDef";

		TransformationInfo info = analyze(methodName);

		MethodData data = transform(info);

		assertThat(data.getInstructions()[10], instanceOf(DupInstruction.class));
		assertThat(data.getInstructions()[11], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[12], instanceOf(StoreInstruction.class));

		StoreInstruction store = (StoreInstruction) data.getInstructions()[12];

		assertEquals(info.getLocalForLabel(null, SUBSTRING, 4), store.getVarIndex());

	}

	@Test
	public void testPhiUses() throws Exception {
		final String methodName = "phiUsesLocalFromUse";

		TransformationInfo info = analyze(methodName);

		MethodData data = transform(info);

		assertThat(data.getInstructions()[17], instanceOf(InvokeInstruction.class));

		assertTrue(data.getHasChanged());
	}

	@Override
	protected Collection<TypeLabel> getTypeLabel() {
		return Arrays.asList(SUBSTRING);
	}
}
