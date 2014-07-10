package de.unifrankfurt.faststring.transform;

import static org.junit.Assert.*;
import org.junit.Test;

import com.ibm.wala.shrikeBT.MethodData;

import de.unifrankfurt.faststring.utils.BaseTransformerTest;

public class TestMethodTransformerUseConversations extends BaseTransformerTest {

	private static final String TEST_CLASS = "TransformationUseTestClass";
	
	@Test
	public void testSimpleSubstring() throws Exception {
		
		TransformationInfo info = analyze("simpleSubstring");
		MethodData data = transform(info);
		
		assertTrue(data.getHasChanged());
		
	}
	
	@Test
	public void testSimpleSubstringWithPhi() throws Exception {
		TransformationInfo info = analyze("simpleSubstringWithPhi");
		MethodData data = transform(info);
	}

	@Override
	public String getTestClass() {
		return TEST_CLASS;
	}
}
