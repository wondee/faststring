package de.unifrankfurt.faststring.transform;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.NewInstruction;
import com.ibm.wala.shrikeBT.StoreInstruction;
import com.ibm.wala.shrikeBT.Util;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.util.StringUtil;
import de.unifrankfurt.faststring.utils.BaseTransformerTest;

public class TestStringBuilderLabel extends BaseTransformerTest {

	@Test
	public void testSimple() throws Exception {
		TransformationInfo info = analyze("testSimple");
		System.out.println(StringUtil.toStringWithLineBreak(info.getReferences()));
		
		MethodData data = transform(info);
		
		assertTrue(data.getHasChanged());
		
		assertThat(data.getInstructions()[5], instanceOf(NewInstruction.class));
		assertThat(data.getInstructions()[8], instanceOf(StoreInstruction.class));
		
		NewInstruction newIns = (NewInstruction) data.getInstructions()[5];
		StoreInstruction store = (StoreInstruction) data.getInstructions()[8];
		
		assertEquals(Util.makeType(STRING_BUILDER.getOptimizedType()), newIns.getType());
		assertEquals(info.getLocalForLabel(null, STRING_BUILDER, 2), store.getVarIndex());
		
	}
	
	@Test
	public void testLoop() throws Exception {
		TransformationInfo info = analyze("testLoop");
		System.out.println(StringUtil.toStringWithLineBreak(info.getReferences()));
		
		MethodData data = transform(info);
		
		assertTrue(data.getHasChanged());
		
		assertThat(data.getInstructions()[10], instanceOf(NewInstruction.class));
		
		NewInstruction newIns = (NewInstruction) data.getInstructions()[10];
		
		assertEquals(Util.makeType(STRING_BUILDER.getOptimizedType()), newIns.getType());
		
	}
	
	@Test
	public void testLoopBuilder() throws Exception {
		TransformationInfo info = analyze("testLoopBuilder");
		System.out.println(StringUtil.toStringWithLineBreak(info.getReferences()));
		
		MethodData data = transform(info);
		
		assertTrue(data.getHasChanged());
		
		assertThat(data.getInstructions()[5], instanceOf(NewInstruction.class));
		assertThat(data.getInstructions()[8], instanceOf(StoreInstruction.class));
		
		NewInstruction newIns = (NewInstruction) data.getInstructions()[5];
		StoreInstruction store = (StoreInstruction) data.getInstructions()[8];
		
		assertEquals(Util.makeType(STRING_BUILDER.getOptimizedType()), newIns.getType());
		assertEquals(info.getLocalForLabel(null, STRING_BUILDER, 2), store.getVarIndex());
		
	}
	
	@Override
	public String getTestClass() {
		return "StringBuilderTestClass";
	}

	@Override
	protected Collection<TypeLabel> getTypeLabel() {
		return Arrays.asList(SUBSTRING, STRING_BUILDER);
	}

}
