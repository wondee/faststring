package de.unifrankfurt.faststring.analysis;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.shrikeBT.IInstruction;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;

public final class TargetApplication {
	// TODO make creation independent of input files (maybe manual)

	private static final Logger LOG = LoggerFactory
			.getLogger(TargetApplication.class);


	private ClassHierarchy classHierarchy;
	private AnalysisScope scope;
	private AnalysisCache cache;

	private AnalysisOptions options;

	private ImmutableSet<IClass> applicationClasses;

//	private static final int LIMIT_SECONDS = 3;
//	private ExecutorService executor = Executors.newFixedThreadPool(1);

	public TargetApplication(String jarName, String exclusionFileName)
			throws IOException, ClassHierarchyException {
		LOG.info("creating analysis scope for {}", jarName);

		File exclusionFile = null;
		if (exclusionFileName != null) {
			LOG.info("using exclusion file {}", exclusionFileName);

			exclusionFile = new File(exclusionFileName);

		}
		scope = AnalysisScopeFactory.createJavaAnalysisScope(jarName,
				exclusionFile);

		LOG.info("building class hierarchy...", jarName);
		classHierarchy = ClassHierarchy.make(scope);

		cache = new AnalysisCache();
		options = new AnalysisOptions();

	}

	public TargetApplication(String fileName) throws IOException,
			ClassHierarchyException {
		this(fileName, null);
	}

	private void initApplicationClasses() {
		Builder<IClass> builder = new ImmutableSet.Builder<IClass>();

		for (IClass cl : classHierarchy) {
			if (scope.isApplicationLoader(cl.getClassLoader())) {

				LOG.info("application class found: {}", cl.getName());

				builder.add(cl);

			}
		}

		applicationClasses = builder.build();
	}

	public ImmutableSet<IClass> getApplicationClasses() {
		if (applicationClasses == null) {
			initApplicationClasses();
		}

		return applicationClasses;
	}

	public IR findIRForMethod(IMethod m) {
		try {
			IInstruction[] instructions = ((IBytecodeMethod) m).getInstructions();
			if (instructions != null) {

				return cache.getSSACache().findOrCreateIR(m, Everywhere.EVERYWHERE,
					options.getSSAOptions());
			}
		} catch (InvalidClassFileException e) {
			e.printStackTrace();
		}
		return null;
	}


	public DefUse findDefUseForMethod(IMethod method) {
		return cache.getSSACache().findOrCreateDU(method,
				Everywhere.EVERYWHERE, options.getSSAOptions());
	}

	public AnalyzedMethod findIRMethodForMethod(IMethod m) {
		LOG.trace("getting ir for {}", m.getSignature());
		IR ir = findIRForMethod(m);
		if (ir == null) {
			LOG.error("could not find ir for method {}", m.getSignature());
			return null;
		}
		DefUse defUse = findDefUseForMethod(m);

		return new AnalyzedMethod(ir, defUse);
	}

	public ClassHierarchy getClassHierachy() {
		return classHierarchy;
	}

}
