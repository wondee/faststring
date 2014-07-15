package de.unifrankfurt.faststring.analysis;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unifrankfurt.faststring.transform.TransformationInfo;
import de.unifrankfurt.faststring.utils.BaseAnalysisTest;
import de.unifrankfurt.faststring.utils.BaseTransformerTest;

public class TestLocateLoad extends BaseTransformerTest {


	@Test
	public void test() throws Exception {
		TransformationInfo info = analyze("test");



	}

	@Override
	public String getTestClass() {
		return "LoadTestClass";
	}
}
