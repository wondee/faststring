package de.unifrankfurt.faststring.analysis.wala;

import java.io.File;
import java.util.Properties;

import com.ibm.wala.cfg.CFGSanitizer;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.properties.WalaProperties;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.viz.DotUtil;
import com.ibm.wala.viz.NodeDecorator;
import com.ibm.wala.viz.PDFViewUtil;

public final class PDFUtil {

	private static final String TARGET_IRS = "target/irs";

	private PDFUtil() {
		// empty
	}

	public static void printToPDF(ClassHierarchy cha, IR ir)
			throws WalaException {

		Properties wp = new Properties();

		File file = new File(TARGET_IRS);

		if (!file.exists()) {
			file.mkdirs();
		}

		wp.put(WalaProperties.OUTPUT_DIR, TARGET_IRS);

		String signature = ir.getMethod().getSignature().replace(';', '#')
				.replace('/', '.');

		String pdfFile = wp.getProperty(WalaProperties.OUTPUT_DIR)
				+ File.separatorChar + signature + ".pdf";
		String dotFile = wp.getProperty(WalaProperties.OUTPUT_DIR)
				+ File.separatorChar + "ir.dt";


		Graph<? extends ISSABasicBlock> g = ir.getControlFlowGraph();

		NodeDecorator labels = PDFViewUtil.makeIRDecorator(ir);
		// if (annotations != null) {
		// labels = new ConcatenatingNodeDecorator(annotations, labels);
		// }

		g = CFGSanitizer.sanitize(ir, cha);

		DotUtil.dotify(g, labels, dotFile, pdfFile, "/usr/bin/dot");

	}

}
