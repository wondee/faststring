package de.unifrankfurt.faststring.analysis.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.ibm.wala.shrikeBT.Disassembler;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.shrikeCT.ClassInstrumenter;
import com.ibm.wala.shrikeBT.shrikeCT.OfflineInstrumenter;

public class PrintTestBytecode {

	private static final Logger LOG = LoggerFactory.getLogger(PrintTestBytecode.class);
	
	public static void main(String[] args) throws Exception {
		OfflineInstrumenter instrumenter = new OfflineInstrumenter();
		
		
		instrumenter.addInputDirectory(new File("../analysis-test/target/classes/"), new File("../analysis-test/target/classes/"));
		
		instrumenter.beginTraversal();
//		PrintWriter writer = new PrintWriter(System.out);
		instrumenter.setPassUnmodifiedClasses(true);
		
		String pathname = "target/bytecode/";
		
		File path = new File(pathname);
		
		if (!path.exists()) {
			path.mkdirs();
		}
		
		ClassInstrumenter ci;
		while ((ci = instrumenter.nextClass()) != null) {
			
			
			List<String> list = Splitter.on("/").splitToList(ci.getReader().getName());			
			String className = list.get(list.size() - 1);
			
			String classOut = pathname + className;
			
			
			for (int i = 0; i < ci.getReader().getMethodCount(); i++) {
				
				MethodData methodData = ci.visitMethod(i);				
				PrintWriter fileWriter = new PrintWriter(new FileOutputStream(classOut  + "." + methodData.getName()));
				
				LOG.info("writing bytecode for {}.{}", className, methodData.getName());
				
				new Disassembler(methodData).disassembleTo(fileWriter);
				fileWriter.flush();
				fileWriter.close();
				
			}
			
		}
		
	}
}
