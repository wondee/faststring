package de.unifrankfurt.faststring.analysis.wala;

import java.io.File;
import java.util.Properties;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.properties.WalaProperties;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.viz.PDFViewUtil;

public final class PDFUtil {

	private PDFUtil() {
		// empty
	}
	
	

	public static void printToPDF(ClassHierarchy cha, IR ir, IMethod m, IClass clazz) throws WalaException {

		Properties wp = null;
	      try {
	        wp = WalaProperties.loadProperties();
	      } catch (WalaException e) {
	        e.printStackTrace();
	        Assertions.UNREACHABLE();
	      }
	      
	      String signature = m.getSignature().replace(';', '#').replace('/', '.');
	      
	      
	      String psFile = wp.getProperty(WalaProperties.OUTPUT_DIR) + File.separatorChar + signature + ".pdf";
	      String dotFile = wp.getProperty(WalaProperties.OUTPUT_DIR) + File.separatorChar + "ir.dt";
	      
	      PDFViewUtil.ghostviewIR(cha, ir, psFile, dotFile, "/usr/bin/dot", "/usr/bin/evince");
		
	}
	
}
