package de.unifrankfurt.faststring.analysis;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Iterables;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisOptions.ReflectionOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.DefaultEntrypoint;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.callgraph.propagation.SSAPropagationCallGraphBuilder;
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
	private ImmutableSet<Entrypoint> entrypoints;
	
	private CallGraph callGraph;

	private PointerAnalysis pointerAnalysis;
	
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
		
//		initCallGraph();
		
	}
	
	
	@SuppressWarnings("unused")
	private void initCallGraph()  {
		CFAStrategy strategy = CFAStrategy.ZERO_ONE_CONTAINER;
		
		LOG.debug("creating pointer analysis...");
		
		AnalysisOptions options = new AnalysisOptions();
		options.setReflectionOptions(ReflectionOptions.NONE);
		options.setEntrypoints(getEntrypoints());
		
		
		LOG.debug("creating builder...");
		ClassHierarchy cha = getClassHierachy();
		SSAPropagationCallGraphBuilder cfaBuilder = strategy.createBuilder(options, cache, cha, scope);
		
		LOG.debug("make callgraph...");
		
		Stopwatch stopwatch = Stopwatch.createStarted();
		
		try {
			cfaBuilder.makeCallGraph(options);
			stopwatch.stop();
			
			LOG.debug("took {}", stopwatch);
			callGraph = cfaBuilder.getCallGraph();
			
			pointerAnalysis = cfaBuilder.getPointerAnalysis();
			
		} catch (Exception e) {
			LOG.error("unable to create call graph", e);
		} 
		
		
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
	
	/**
	 * @return all public methods in application scope classes as {@link Entrypoint}
	 */
	public Iterable<Entrypoint> getEntrypoints() {
		if (entrypoints == null) {
			initEntrypoints();
		}
		
		return entrypoints;
	}
	
	public Entrypoint getEntrypoint(final String clName, final String mName) {
		
		IClass cl = Iterables.find(applicationClasses, new Predicate<IClass>() {
			
			public boolean apply(IClass input) {
				System.out.println(input.getName().toString() + "=" + clName);
				return input.getName().toString().endsWith(clName);
				
			};
		});
		
		IMethod method = Iterables.find(cl.getDeclaredMethods(), new Predicate<IMethod>() {
			
			@Override
			public boolean apply(IMethod input) {
				return input.getName().toString().equals(mName);
			}
		});
		
		
		
		
		return new DefaultEntrypoint(method, classHierarchy);
		
	}

	private void initEntrypoints() {
		Builder<Entrypoint> builder = new ImmutableSet.Builder<Entrypoint>();
		
		for(IClass cl : applicationClasses) {
			for (IMethod m : cl.getDeclaredMethods()) {
				if (m.isPublic()) {
					LOG.debug("adding entrypoint {}", m.getSignature());
					
					// TODO maybe use a ArgumentTypeEntryPoint
					DefaultEntrypoint entrypoint = new DefaultEntrypoint(m, classHierarchy);
					
					builder.add(entrypoint);
				}
			}
		}
		this.entrypoints = builder.build();
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

	public ClassHierarchy getClassHierachy() {
		return classHierarchy;
	}

	public AnalysisScope getScope() {
		return scope;
	}

	public PointerAnalysis getPointerAnalysis() {
		return pointerAnalysis;
	}
	
	public CallGraph getCallGraph() {
		return callGraph;
	}


	
}
