package de.unifrankfurt.faststring.analysis.test.util;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hamcrest.collection.IsEmptyIterable;
import org.hamcrest.collection.IsIterableContainingInOrder;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.wala.shrikeBT.Disassembler;
import com.ibm.wala.shrikeBT.MethodData;

import de.unifrankfurt.faststring.analysis.TargetApplication;
import de.unifrankfurt.faststring.analysis.TypeLabelConfigParser;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public final class TestUtilities {

	private static final String TEST_EXCLUSION_FILE = "testExclusion.txt";
	private static final String TEST_JAR_FILE = "target/test.jar";



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

	public static TargetApplication loadTestJar(String jarFile) {
		try {
			return new TargetApplication(jarFile,TEST_EXCLUSION_FILE);
		} catch (Exception e) {
			throw new TestInitializingException("Failed to load testclasses", e);
		}
	}


	public static TargetApplication loadTestJar() {
		return loadTestJar(TEST_JAR_FILE);
	}

	public static String replaceInitChars(String methodName) {
		return methodName.replace('<', '{').replace('>', '}');
	}

	public static String createFileName(String className, String method) {
		List<String> list = Splitter.on("/").splitToList(className);
		String name = list.get(list.size() - 1);

		return name + "." + replaceInitChars(method);
	}

	private static final Map<String, TypeLabel> labelCache = Maps.newHashMap();

	public static TypeLabel loadTestLabel(String name) {
		TypeLabel label = labelCache.get(name);

		if (label == null) {
			label = new TypeLabelConfigParser().parseFile(name + ".type");
			labelCache.put(name, label);
		}

		return label;
	}

	public static List<TypeLabel> loadTestLabels(String ... names) {
		List<TypeLabel> labels = Lists.newLinkedList();

		for (String name : names) {
			labels.add(loadTestLabel(name));
		}

		return labels;
	}

	public static String toStringBytecode(MethodData methodData) throws IOException {
		StringWriter writer = new StringWriter();
		new Disassembler(methodData).disassembleTo("  ", writer);

		return writer.toString();
	}

}
