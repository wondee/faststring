package de.unifrankfurt.faststring.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wala.cfg.CFGSanitizer;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.properties.WalaProperties;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.viz.DotUtil;
import com.ibm.wala.viz.NodeDecorator;
import com.ibm.wala.viz.PDFViewUtil;

import de.unifrankfurt.faststring.analysis.TargetApplication;

public class PrintTestIRs {

	/**
	 * main method to print out all ir for the test classes
	 * @param args no args defined
	 * @throws IOException 
	 * @throws WalaException 
	 */
	public static void main(String[] args) throws IOException, WalaException {
		String pathname = TARGET_IRS;
		
		if (args.length > 0) {
			pathname = args[0];
		}
		
		
		TargetApplication targetApplication = TestUtilities.loadTestClasses();
		
		for (IClass clazz : targetApplication.getApplicationClasses()) {
			for (IMethod m : clazz.getDeclaredMethods()) {
				printToPDF(pathname, targetApplication.getClassHierachy(), targetApplication.findIRForMethod(m));
			}
		}
	}
	
	private static final Logger LOG = LoggerFactory.getLogger("print all IRs");
	
	private static final String PRINT_PROPERTIES = "/print.properties";
	private static final String TARGET_IRS = "../analysis-test/target/irs";

	private static void printToPDF(String pathname, ClassHierarchy cha, IR ir) throws WalaException, IOException {
		
		Properties wp = loadProperties();
		
		File file = new File(pathname);

		if (!file.exists()) {
			file.mkdirs();
		}

		wp.put(WalaProperties.OUTPUT_DIR, TARGET_IRS);

		System.out.println(ir.getMethod().getDeclaringClass().getName());
		
		String pdfFile = wp.getProperty(WalaProperties.OUTPUT_DIR) + 
						 File.separatorChar + 
						 TestUtilities.createFileName(ir.getMethod().getDeclaringClass().getName().toString(), 
								 ir.getMethod().getName().toString()) + 
								 ".pdf";
		
		System.out.println(pdfFile);
		
		String dotFile = wp.getProperty(WalaProperties.OUTPUT_DIR)
				+ File.separatorChar + "ir.dt";
		if (new File(pdfFile).exists()) {
			LOG.info("skipping {}", pdfFile);
		} else {
			Graph<ISSABasicBlock> g = ir.getControlFlowGraph();
	
			NodeDecorator<ISSABasicBlock> labels = PDFViewUtil.makeIRDecorator(ir);
			g = CFGSanitizer.sanitize(ir, cha);
	
			DotUtil.dotify(g, labels, dotFile, pdfFile, (String)wp.get("dot"));
		}
	}

	private static Properties loadProperties() throws IOException {
		
		Properties wp = new Properties();
		
		InputStream in = PrintTestIRs.class.getResourceAsStream(PRINT_PROPERTIES);
		if (in == null) {
			throw new FileNotFoundException("no file " + PRINT_PROPERTIES + " was found on classpath");
		}
		wp.load(in);
		return wp;
		
	}
}
