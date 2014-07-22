package de.unifrankfurt.faststring.transform;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import com.ibm.wala.shrikeBT.MethodData;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.core.label.BuiltInTypes;
import de.unifrankfurt.faststring.utils.BaseTransformerTest;

public class TestParserMethod extends BaseTransformerTest{

	@Test
	public void testParser() throws Exception {

		TransformationInfo info = analyze("parse");

		MethodData data = transform(info);

		assertTrue(data.getHasChanged());

	}

	@Override
	public String getTestClass() {
		return "ParserTest";
	}


	@Override
	protected Collection<TypeLabel> getTypeLabel() {
		return Arrays.asList(BuiltInTypes.SUBSTRING);
	}
	
}
