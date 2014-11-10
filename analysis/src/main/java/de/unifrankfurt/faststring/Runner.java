package de.unifrankfurt.faststring;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.cha.ClassHierarchyException;

import de.unifrankfurt.faststring.analysis.AnalysisResult;
import de.unifrankfurt.faststring.analysis.AnalyzedMethod;
import de.unifrankfurt.faststring.analysis.MethodAnalyzer;
import de.unifrankfurt.faststring.analysis.TargetApplication;
import de.unifrankfurt.faststring.analysis.TypeLabelConfigParser;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.util.FileUtil;
import de.unifrankfurt.faststring.transform.JarManager;

/**
 * Main class for the optimization system.
 * <p>
 * Takes as input a list of labels to work with, a program in form of a jar file and an optional exclusion file which
 * holds a set of packages which will not be optimized.
 *
 * @author markus
 *
 */
public class Runner {
	private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

	@Parameter(names = "-labels", description = "The optimizing labels to be used", variableArity = true, required = true, converter = TypeLabelCreator.class)
	private List<TypeLabel> typeList;

	@Parameter(names = "-jar", required = true)
	private String jarFile;

	@Parameter(names = "-exclusion")
	private String exclusionFile;

	public static void main(String[] args) {
		Runner runner = new Runner();

		new JCommander(runner, args);

		runner.run();
	}

	private void run() {
		List<TypeLabel> filteredList = Lists.newLinkedList(Iterables.filter(typeList, Predicates.notNull()));

		if (filteredList.isEmpty()) {
			throw new IllegalArgumentException("no label was found");
		}

		try {
			TargetApplication targetApplication = new TargetApplication(jarFile, exclusionFile);

			Map<String, AnalysisResult> analysisResult = Maps.newTreeMap();
			int i = 1;
			for (IClass clazz : targetApplication.getApplicationClasses()) {
				LOG.info("analyzing class ({}/{}) {} ", i++, targetApplication.getApplicationClasses().size(),
						clazz.getName());
				processClass(filteredList, targetApplication, analysisResult, clazz);
			}

			LOG.info("creating outputfile");

			FileUtil.checkOutputPath("faststring-output/");

			Path outputFile = Paths.get("faststring-output/_optimizedMethods");
			Files.write(outputFile, analysisResult.keySet(), Charset.defaultCharset());

			LOG.info("finished analyzing; starting transforming. {} results created", analysisResult.size());
			JarManager.createForJar(jarFile, analysisResult, targetApplication.getClassHierarchyStore()).process();

		} catch (ClassHierarchyException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private void processClass(List<TypeLabel> filteredList,
			TargetApplication targetApplication,
			Map<String, AnalysisResult> analysisResult, IClass clazz) {

		for (IMethod m : clazz.getDeclaredMethods()) {
			try {
				AnalyzedMethod method = targetApplication.findIRMethodForMethod(m);

				if (method != null) {
					MethodAnalyzer analyzer = new MethodAnalyzer(method, filteredList);

					AnalysisResult candidates = analyzer.analyze();

					if (!candidates.isEmpty()) {
						analysisResult.put(m.getSignature(), candidates);
					}
				} else {
					LOG.error("method is skipped");
				}
			} catch (Exception e) {
				FileUtil.handleError(e, m.getSignature());
			}
		}
	}

	public static class TypeLabelCreator implements IStringConverter<TypeLabel> {

		@Override
		public TypeLabel convert(String string) {
			TypeLabelConfigParser parser = new TypeLabelConfigParser();
			try {
				TypeLabel label = parser.parseFile(string + ".type");
				LOG.info("found label {}", label);
				return label;
			} catch (Exception e) {
				LOG.error("could not load type label", string, e);
			}

			return null;
		}

	}

}
