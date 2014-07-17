package de.unifrankfurt.faststring.analysis.test.util;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.collection.IsEmptyIterable;
import org.hamcrest.collection.IsIterableContainingInOrder;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import de.unifrankfurt.faststring.analysis.TargetApplication;

public final class TestUtilities {

//	private static final String TEST_RES = "../analysis-test/src/main/resources/";
	
	private static final String TEST_JAR_SCOPE_FILE = "testJarScope.txt";
	private static final String TEST_EXCLUSION_FILE = "testExclusion.txt";	
	
	
	
	private TestUtilities() {
		// empty
	}
	
	public static <E extends Comparable<E>> void assertList(Iterable<E> actual, Object...expected) {
		if (expected.length == 0) {
			assertListEmpty(actual);
		} else {
		
			List<E> list = Lists.newArrayList(actual);
			Collections.sort(list);
			Arrays.sort(expected);
			
			assertThat(list, IsIterableContainingInOrder.contains(expected));
		}
	}
	
	public static <E extends Comparable<E>> void assertList(Iterable<E> actual, List<E> expected) {
		assertList(actual, expected.toArray());
	}
	
	
	public static <E extends Comparable<E>> void assertListEmpty(Iterable<E> actual) {
		assertThat(actual, IsEmptyIterable.emptyIterable());
	}
	
	
	public static TargetApplication loadTestClasses() {
		return loadTestJar();
	}
	
	public static TargetApplication loadTestJar() {
		try {
			return new TargetApplication(TEST_JAR_SCOPE_FILE,TEST_EXCLUSION_FILE);
		} catch (Exception e) {
			throw new TestInitializingException("Failed to load testclasses", e);
		}
	}
	
	public static String replaceInitChars(String methodName) {
		return methodName.replace('<', '{').replace('>', '}');
	}
	
	public static String createFileName(String className, String method) {
		List<String> list = Splitter.on("/").splitToList(className);			
		String name = list.get(list.size() - 1);
		
		return name + "." + replaceInitChars(method);
	}
}
