package de.unifrankfurt.faststring.analysis.wala;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap.Builder;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.config.AnalysisScopeReader;

/**
 * base test class for all analysis test cases. Takes care creating the {@link ClassHierarchy} and
 * provides methods for easily retrieving {@link IR}s and {@link IClass}es
 * 
 * @author markus
 *
 */
public abstract class BaseAnalysisTest {

	private static final Logger LOG = LoggerFactory.getLogger(BaseAnalysisTest.class);
	
	private static final String TEST_RES = "src/test/resources/";
	
	private static final String TEST_SCOPE_FILE = TEST_RES + "testScope.txt";
	private static final String TEST_EXCLUSION_FILE = TEST_RES + "testExclusion.txt";	
	
	private static AnalysisScope scope = null;

	private static ClassHierarchy classHierachy = null;

	private static Map<String, IClass> testClassMap = null;

	private static AnalysisCache cache = new AnalysisCache();
	private static AnalysisOptions options = new AnalysisOptions();
	
	@BeforeClass
	public static void loadClassHierachie() throws IOException, ClassHierarchyException {
		if (scope == null) {
			LOG.info("initializing analysis scope");
			
			scope = AnalysisScopeReader.readJavaScope(TEST_SCOPE_FILE, 
					new File(TEST_EXCLUSION_FILE), 
					BaseAnalysisTest.class.getClassLoader());
		}
		if (classHierachy == null) {
			LOG.info("creating class hierachy...");
			
			classHierachy  = ClassHierarchy.make(scope);
		}
	}
	
	private static void initTestClasses() {
		checkNotNull(classHierachy, "classHierachy is null. This might be "
				+ "cause by not calling loadClassHierachie before");
		
		LOG.info("initializing test classes");

		Builder<String, IClass> builder = new Builder<String, IClass>();
		
		for (IClass cl : classHierachy) {
			if (scope.isApplicationLoader(cl.getClassLoader())) {
				
				String className = cl.getName().getClassName().toString();
				
				LOG.info("test class {} found", className);
				builder.put(className, cl);
				
			}	
		}
		
		testClassMap = builder.build();
	}
	
	
	protected static IClass lookUpClass(String name) {
		checkNotNull(name, "name of class must not be null");
		
		if (testClassMap == null) {
			initTestClasses();
		}
		
		IClass cl = testClassMap.get(name);
		
		if (cl != null) {
			return cl;
		} else {
			throw new IllegalArgumentException(
					String.format("no class with name '%s' could be found in "
						+ "the test class hierachie", 
						name));
		}
		
	}
	
	protected static Map<String, IR> getIRsForClass(String name) {
		
		Builder<String, IR> builder = new Builder<String, IR>();
		
		for (IMethod m : lookUpClass(name).getDeclaredMethods()) {
			IR ir = cache.getSSACache().findOrCreateIR(m, Everywhere.EVERYWHERE, options.getSSAOptions());
			
			builder.put(m.getName().toString(), ir);
		}

		return builder.build();
		
	}
	
	protected static IR getIR(String className, String methodName) {
		IR ir = getIRsForClass(className).get(methodName);
		
		checkNotNull(ir, "no ir found for class %s and method %s", className, methodName);
		
		return ir;
	}
	
	/**
	 * main method to print out all ir for the test classes
	 * @param args no args defined
	 * @throws IOException 
	 * @throws WalaException 
	 */
	public static void main(String[] args) throws IOException, WalaException {
		loadClassHierachie();
		initTestClasses();
		
		for (String name : testClassMap.keySet()) {
			for (IR ir : getIRsForClass(name).values()) {
				System.out.println("---------------");
				System.out.println(ir);
				PDFUtil.printToPDF(classHierachy, ir);
			}
		}
	}
	
}
