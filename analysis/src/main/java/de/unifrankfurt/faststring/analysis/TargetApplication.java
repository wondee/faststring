package de.unifrankfurt.faststring.analysis;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.util.config.AnalysisScopeReader;

public final class TargetApplication {
	// TODO make creation independent of input files (maybe manual)
	
	
	private static final Logger LOG = LoggerFactory.getLogger(TargetApplication.class);
	
	private ClassHierarchy classHierarchy;
	private AnalysisScope scope;
	private AnalysisCache cache;

	private AnalysisOptions options;
	
	private ImmutableSet<IClass> applicationClasses;

	public TargetApplication(String fileName, String exclusionFileName) throws IOException, ClassHierarchyException {
		LOG.info("reading analysis scope file {}", fileName);
		
		File exclusionFile = null;
		if (exclusionFileName != null) {
			LOG.info("using exclusion file {}", exclusionFileName);
			
			exclusionFile = new File(exclusionFileName);
			
		}
		scope = AnalysisScopeReader.readJavaScope(fileName, exclusionFile, TargetApplication.class.getClassLoader());		
		LOG.info("building class hierarchy...", fileName);
		classHierarchy = ClassHierarchy.make(scope);
		
		cache = new AnalysisCache();
		options = new AnalysisOptions();
		
		initApplicationClasses();
		
	}

	public TargetApplication(String fileName) throws IOException, ClassHierarchyException {
		this(fileName, null);
	}


	private void initApplicationClasses() {
		Builder<IClass> builder = new ImmutableSet.Builder<IClass>();
		
		for (IClass cl : classHierarchy) {
			if (scope.isApplicationLoader(cl.getClassLoader()))  {
				
				LOG.info("application class found: {}", cl.getName());
				
				builder.add(cl);
				
			}
		}
		
		applicationClasses = builder.build();
	}


	
	public ImmutableSet<IClass> getApplicationClasses() {
		return applicationClasses;
	}

	public IR findIRForMethod(IMethod m) {		
		return cache.getSSACache().findOrCreateIR(m, Everywhere.EVERYWHERE, options.getSSAOptions());
	}

	public DefUse findDefUseForMethod(IMethod method) {
		return cache.getSSACache().findOrCreateDU(method, Everywhere.EVERYWHERE, options.getSSAOptions()) ;
	}

	public AnalyzedMethod findIRMethodForMethod(IMethod m) {
		return new AnalyzedMethod(findIRForMethod(m), findDefUseForMethod(m));
	}
	
	public ClassHierarchy getClassHierachy() {
		return classHierarchy;
	}

	
}
