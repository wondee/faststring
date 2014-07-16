package de.unifrankfurt.faststring.transform;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.Test;

import com.ibm.wala.shrikeBT.InvokeInstruction;
import com.ibm.wala.shrikeBT.LoadInstruction;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.StoreInstruction;

import de.unifrankfurt.faststring.analysis.label.BuiltInTypes;
import de.unifrankfurt.faststring.utils.BaseTransformerTest;

public class TestMethodTransformerUseConversations extends BaseTransformerTest {

	private static final String TEST_CLASS = "TransformationUseTestClass";

	@Test
	public void testSimpleSubstring() throws Exception {

		TransformationInfo info = analyze("simpleSubstring");
		MethodData data = transform(info);

		assertTrue(data.getHasChanged());
		
		assertThat(data.getInstructions()[2], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[3], instanceOf(StoreInstruction.class));
		assertThat(data.getInstructions()[5], instanceOf(LoadInstruction.class));

		StoreInstruction store = (StoreInstruction)data.getInstructions()[3];
		LoadInstruction load = (LoadInstruction) data.getInstructions()[5];
		
		assertEquals(store.getVarIndex(), load.getVarIndex());
		assertEquals(info.getLocalForLabel(null, BuiltInTypes.SUBSTRING, 1), load.getVarIndex());
		
	}

	@Test
	public void testSimpleSubstringWithPhi() throws Exception {
		TransformationInfo info = analyze("simpleSubstringWithPhi");
		MethodData data = transform(info);

		assertTrue(data.getHasChanged());

	}

	@Override
	public String getTestClass() {
		return TEST_CLASS;
	}
}
