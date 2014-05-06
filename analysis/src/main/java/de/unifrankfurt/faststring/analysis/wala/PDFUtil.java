package de.unifrankfurt.faststring.analysis.wala;

import java.io.File;
import java.util.Properties;

import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.properties.WalaProperties;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.viz.PDFViewUtil;

public final class PDFUtil {

	private static final String TARGET_IRS = "target/irs";

	private PDFUtil() {
		// empty
	}

	public static void printToPDF(ClassHierarchy cha, IR ir) throws WalaException {

		Properties wp = new Properties();

		File file = new File(TARGET_IRS);
		
		if (!file.exists()) {
			file.mkdirs();
		}
		
		wp.put(WalaProperties.OUTPUT_DIR, TARGET_IRS);
		
		String signature = ir.getMethod().getSignature().replace(';', '#').replace('/', '.');

		String psFile = wp.getProperty(WalaProperties.OUTPUT_DIR)
				+ File.separatorChar + signature + ".pdf";
		String dotFile = wp.getProperty(WalaProperties.OUTPUT_DIR)
				+ File.separatorChar + "ir.dt";

		PDFViewUtil.ghostviewIR(cha, ir, psFile, dotFile, "/usr/bin/dot",
				"/usr/bin/evince");

	}

}
