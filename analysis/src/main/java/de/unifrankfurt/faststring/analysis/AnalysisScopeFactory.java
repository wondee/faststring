package de.unifrankfurt.faststring.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.config.FileOfClasses;

/**
 * Factory for creating a {@link AnalysisScope} based on a input jar file.
 * <p>
 * Little hack because WALA only supports input files for creating a scope
 *
 * @author markus
 *
 */
public class AnalysisScopeFactory {

	@SuppressWarnings("resource")
	public static AnalysisScope createJavaAnalysisScope(String jarName, File exclusionsFile) {

		AnalysisScope scope = AnalysisScope.createJavaAnalysisScope();
		InputStream fs = null;
		try {
			ClassLoader classLoader = AnalysisScopeFactory.class.getClassLoader();
			AnalysisScopeReader.processScopeDefLine(scope, classLoader, "Primordial,Java,stdlib,none");
			AnalysisScopeReader.processScopeDefLine(scope, classLoader, "Application,Java,jarFile," + jarName);

			if (exclusionsFile != null) {
				fs = (exclusionsFile.exists()) ? new FileInputStream(exclusionsFile) : classLoader.getResourceAsStream(exclusionsFile.getName());
				scope.setExclusions(new FileOfClasses(fs));
			}

			return scope;
		} catch (IOException e) {
			throw new IllegalArgumentException("error while creating analysis scope", e);
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
