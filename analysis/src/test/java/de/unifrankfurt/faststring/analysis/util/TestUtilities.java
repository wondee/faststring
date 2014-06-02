package de.unifrankfurt.faststring.analysis.util;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.collection.IsEmptyIterable;
import org.hamcrest.collection.IsIterableContainingInOrder;

import com.google.common.collect.Lists;
import com.ibm.wala.ipa.cha.ClassHierarchyException;

import de.unifrankfurt.faststring.analysis.TargetApplication;

public final class TestUtilities {

	private static final String TEST_RES = "../analysis-test/src/main/resources/";
	
	private static final String TEST_SCOPE_FILE = TEST_RES + "testScope.txt";
	private static final String TEST_EXCLUSION_FILE = TEST_RES + "testExclusion.txt";	
	
	private TestUtilities() {
		// empty
	}
	
	public static <E extends Comparable<E>> void assertList(Iterable<E> actual, Object...expected) {
		List<E> list = Lists.newArrayList(actual);
		Collections.sort(list);
		Arrays.sort(expected);
		
		assertThat(list, IsIterableContainingInOrder.contains(expected));
	}
	
	public static <E extends Comparable<E>> void assertList(Iterable<E> actual, List<E> expected) {
		assertList(actual, expected.toArray());
	}
	
	
	public static <E extends Comparable<E>> void assertListEmpty(Iterable<E> actual) {
		assertThat(actual, IsEmptyIterable.emptyIterable());
	}
	
	
	public static TargetApplication loadTestClasses() {
		try {
			return new TargetApplication(TEST_SCOPE_FILE,TEST_EXCLUSION_FILE);
		} catch (ClassHierarchyException | IOException e ) {
			throw new TestInitializingException("Failed to load testclasses", e);
		}
	}
}
