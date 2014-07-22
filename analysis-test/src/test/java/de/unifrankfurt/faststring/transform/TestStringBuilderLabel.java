package de.unifrankfurt.faststring.transform;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import com.ibm.wala.shrikeBT.MethodData;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.util.StringUtil;
import de.unifrankfurt.faststring.core.label.BuiltInTypes;
import de.unifrankfurt.faststring.utils.BaseTransformerTest;

public class TestStringBuilderLabel extends BaseTransformerTest {

	@Test
	public void testSimple() throws Exception {
		TransformationInfo info = analyze("testSimple");
		System.out.println(StringUtil.toStringWithLineBreak(info.getReferences()));
		
		MethodData data = transform(info);
		
		assertTrue(data.getHasChanged());
		
	}
	
	@Test
	public void testLoop() throws Exception {
		TransformationInfo info = analyze("testLoop");
		System.out.println(StringUtil.toStringWithLineBreak(info.getReferences()));
		
		MethodData data = transform(info);
		
		assertTrue(data.getHasChanged());
		
	}
	
	@Test
	public void testLoopBuilder() throws Exception {
		TransformationInfo info = analyze("testLoopBuilder");
		System.out.println(StringUtil.toStringWithLineBreak(info.getReferences()));
		
		MethodData data = transform(info);
		
		assertTrue(data.getHasChanged());
		
	}
	
	@Override
	public String getTestClass() {
		return "StringBuilderTestClass";
	}

	@Override
	protected Collection<TypeLabel> getTypeLabel() {
		return Arrays.asList(BuiltInTypes.SUBSTRING, BuiltInTypes.STRING_BUILDER);
	}

}
