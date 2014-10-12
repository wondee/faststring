package de.unifrankfurt.faststring.transform;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import com.ibm.wala.shrikeBT.ConstantInstruction;
import com.ibm.wala.shrikeBT.DupInstruction;
import com.ibm.wala.shrikeBT.InvokeInstruction;
import com.ibm.wala.shrikeBT.LoadInstruction;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.ReturnInstruction;
import com.ibm.wala.shrikeBT.StoreInstruction;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.util.StringUtil;
import de.unifrankfurt.faststring.utils.BaseTransformerTest;

public class TestMethodTransformerUseConversations extends BaseTransformerTest {

	private static final String TEST_CLASS = "TransformationUseTestClass";

	@Test
	public void testSimpleSubstring() throws Exception {

		TransformationInfo info = analyze("substring");
		MethodData data = transform(info);

		assertTrue(data.getHasChanged());

		assertThat(data.getInstructions()[2], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[3], instanceOf(StoreInstruction.class));
		assertThat(data.getInstructions()[5], instanceOf(LoadInstruction.class));

		StoreInstruction store = (StoreInstruction)data.getInstructions()[3];
		LoadInstruction load = (LoadInstruction) data.getInstructions()[5];

		assertEquals(store.getVarIndex(), load.getVarIndex());
		assertEquals(info.getLocalForLabel(null, SUBSTRING, 1), load.getVarIndex());

	}

	@Test
	public void testSimpleSubstringWithPhi() throws Exception {
		TransformationInfo info = analyze("substringWithPhi");
		MethodData data = transform(info);

		assertTrue(data.getHasChanged());

		assertThat(data.getInstructions()[10], instanceOf(InvokeInstruction.class));
	}

	@Test
	public void testSubstringWithPhiLocal() throws Exception {
		TransformationInfo info = analyze("substringWithPhiLocal");
		MethodData data = transform(info);

		assertTrue(data.getHasChanged());

		assertThat(data.getInstructions()[10], instanceOf(DupInstruction.class));
		assertThat(data.getInstructions()[11], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[12], instanceOf(StoreInstruction.class));
		assertThat(data.getInstructions()[14], instanceOf(LoadInstruction.class));

		StoreInstruction store = (StoreInstruction)data.getInstructions()[12];
		LoadInstruction load = (LoadInstruction) data.getInstructions()[14];

		assertEquals(store.getVarIndex(), load.getVarIndex());
		assertEquals(info.getLocalForLabel(null, SUBSTRING, 4), load.getVarIndex());
	}

	@Test
	public void testSubstringReturned() throws Exception {
		TransformationInfo info = analyze("substringReturned");
		MethodData data = transform(info);

		assertTrue(data.getHasChanged());

		assertThat(data.getInstructions()[1], instanceOf(DupInstruction.class));
		assertThat(data.getInstructions()[2], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[3], instanceOf(StoreInstruction.class));
		assertThat(data.getInstructions()[5], instanceOf(LoadInstruction.class));
		assertThat(data.getInstructions()[8], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[9], instanceOf(ReturnInstruction.class));

		StoreInstruction store = (StoreInstruction)data.getInstructions()[3];
		LoadInstruction load = (LoadInstruction) data.getInstructions()[5];

		assertEquals(store.getVarIndex(), load.getVarIndex());
		assertEquals(info.getLocalForLabel(null, SUBSTRING, 1), load.getVarIndex());
	}


	@Test
	public void testSubstringReturnedWithLocal() throws Exception {
		TransformationInfo info = analyze("substringReturnedWithLocal");
		MethodData data = transform(info);

		assertTrue(data.getHasChanged());

		assertThat(data.getInstructions()[1], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[4], instanceOf(StoreInstruction.class));
		assertThat(data.getInstructions()[5], instanceOf(LoadInstruction.class));
		assertThat(data.getInstructions()[6], instanceOf(InvokeInstruction.class));

		StoreInstruction store = (StoreInstruction)data.getInstructions()[4];
		LoadInstruction load = (LoadInstruction) data.getInstructions()[5];

		assertEquals(store.getVarIndex(), load.getVarIndex());
		assertEquals(info.getLocalForLabel(null, SUBSTRING, 1), load.getVarIndex());
	}



	@Test
	public void testSubstringUsed() throws Exception {
		TransformationInfo info = analyze("substringUsed");
		MethodData data = transform(info);

		assertTrue(data.getHasChanged());

		assertThat(data.getInstructions()[1], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[3], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[4], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[5], instanceOf(InvokeInstruction.class));

		assertEquals(7, data.getInstructions().length);
	}

	@Test
	public void testSubstringBinaryop() throws Exception {
		TransformationInfo info = analyze("substringWithBinaryOp");
		MethodData data = transform(info);

		assertTrue(data.getHasChanged());

//		assertThat(data.getInstructions()[1], instanceOf(InvokeInstruction.class));
//		assertThat(data.getInstructions()[3], instanceOf(InvokeInstruction.class));
//		assertThat(data.getInstructions()[4], instanceOf(InvokeInstruction.class));
//		assertThat(data.getInstructions()[5], instanceOf(InvokeInstruction.class));

//		assertEquals(7, data.getInstructions().length);
	}


	@Test
	public void testStartsWith() throws Exception {
		TransformationInfo info = analyze("startsWith");

		MethodData data = transform(info);

		assertTrue(data.getHasChanged());

		assertThat(data.getInstructions()[5], instanceOf(LoadInstruction.class));
		assertThat(data.getInstructions()[6], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[7], instanceOf(ConstantInstruction.class));
	}

	@Test
	public void testConversation() throws Exception {

		TransformationInfo info = analyze("substringConversation");

		System.out.println(StringUtil.toStringWithLineBreak(info.getReferences()));

		MethodData data = transform(info);

		assertTrue(data.getHasChanged());

		assertThat(data.getInstructions()[1], instanceOf(ConstantInstruction.class));
		assertThat(data.getInstructions()[2], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[4], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[5], instanceOf(InvokeInstruction.class));
		assertThat(data.getInstructions()[6], instanceOf(InvokeInstruction.class));

	}

	@Test
	public void testLengthCharAt() throws Exception {
		TransformationInfo info = analyze("lengthCharAt");

		System.out.println(StringUtil.toStringWithLineBreak(info.getReferences()));

		MethodData data = transform(info, true);

		assertTrue(data.getHasChanged());

		assertThat(data.getInstructions()[6], instanceOf(LoadInstruction.class));

		LoadInstruction load = (LoadInstruction) data.getInstructions()[6];

		assertEquals(info.getLocalForLabel(null, SUBSTRING, 1), load.getVarIndex());
	}

	@Override
	public String getTestClass() {
		return TEST_CLASS;
	}


	@Override
	protected Collection<TypeLabel> getTypeLabel() {
		return Arrays.asList(SUBSTRING);
	}
}
