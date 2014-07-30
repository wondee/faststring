package de.unifrankfurt.faststring.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Map;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap.Builder;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.ShrikeClass;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.shrikeBT.shrikeCT.ClassInstrumenter;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;

import de.unifrankfurt.faststring.analysis.AnalyzedMethod;
import de.unifrankfurt.faststring.analysis.TargetApplication;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.test.util.TestUtilities;

/**
 * base test class for all analysis test cases. Takes care creating the {@link ClassHierarchy} and
 * provides methods for easily retrieving {@link IR}s and {@link IClass}es.
 * 
 * This class is for test proposes only! 
 * 
 * @author markus
 *
 */
public abstract class BaseAnalysisTest {

	protected static final TypeLabel SUBSTRING = TestUtilities.loadTestLabel("SubstringString");
	protected static final TypeLabel STRING_BUILDER = TestUtilities.loadTestLabel("StringListBuilder");
	
	private static final Logger LOG = LoggerFactory.getLogger(BaseAnalysisTest.class);
	
	private static TargetApplication targetApplication;

	
	private static Map<String, IClass> testClassMap = null;
	
	@BeforeClass
	public static void loadClassHierachie() throws IOException, ClassHierarchyException {
		if (targetApplication == null) {
			targetApplication = TestUtilities.loadTestClasses();
		}
		
	}
	
	private static void initTestClasses() {
		checkNotNull(targetApplication, "classHierachy is null. This might be "
				+ "caused by not calling loadClassHierachie before");
		
		LOG.info("creating test classes map");

		Builder<String, IClass> builder = new Builder<String, IClass>();
		
		for (IClass cl : targetApplication.getApplicationClasses()) {
				
			String className = cl.getName().getClassName().toString();
			builder.put(className, cl);
				
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
			
			IR ir = targetApplication.findIRForMethod(m);
			builder.put(m.getName().toString(), ir);
		}

		return builder.build();
		
	}
	
	protected static IR getIR(String className, String methodName) {
		IR ir = getIRsForClass(className).get(methodName);
		
		checkNotNull(ir, "no ir found for class %s and method %s", className, methodName);
		
		return ir;
	}
	
	protected static DefUse getDefUse(String className, String methodName) {
		return new DefUse(getIR(className, methodName));
	}
	
	protected static IMethod getMethod(String className, String methodName) {
		return getIR(className, methodName).getMethod();
	}
	
	protected static AnalyzedMethod getIRMethod(String className, String methodName) {
		return targetApplication.findIRMethodForMethod(getMethod(className, methodName));
	}
	
	public static TargetApplication getTargetApplication() {
		return targetApplication;
	}
	
	protected static ClassInstrumenter getClassInstrumenter(String className) {
		ShrikeClass shrikeClass = (ShrikeClass) lookUpClass(className);
		
		try {
			return new ClassInstrumenter(className, shrikeClass.getReader());
		} catch (InvalidClassFileException e) {
			throw new IllegalStateException(e);
		}
	}
}
