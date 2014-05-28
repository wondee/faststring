package de.unifrankfurt.faststring.analysis.util;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.collection.IsEmptyIterable;
import org.hamcrest.collection.IsIterableContainingInOrder;

public final class TestUtilities {

	private TestUtilities() {
		// empty
	}
	
	public static <E extends Comparable<E>> void assertList(List<E> actual, Object...expected) {
		
		Collections.sort(actual);
		Arrays.sort(expected);
		
		assertThat(actual, IsIterableContainingInOrder.contains(expected));
	}
	
	public static <E extends Comparable<E>> void assertList(List<E> actual, List<E> expected) {
		assertList(actual, expected.toArray());
	}
	
	
	public static <E extends Comparable<E>> void assertListEmpty(List<E> actual) {
		assertThat(actual, IsEmptyIterable.emptyIterable());
	}
	
}
