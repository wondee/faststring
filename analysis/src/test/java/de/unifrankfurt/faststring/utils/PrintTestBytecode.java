package de.unifrankfurt.faststring.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wala.shrikeBT.Disassembler;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.shrikeCT.ClassInstrumenter;
import com.ibm.wala.shrikeBT.shrikeCT.OfflineInstrumenter;

public class PrintTestBytecode {

	private static final Logger LOG = LoggerFactory.getLogger(PrintTestBytecode.class);
	
	public static void main(String[] args) throws Exception {
		String pathname = "target/bytecode/";
		String inputDir = "../analysis-test/target/classes/";
		
		if (args.length > 0) {
			pathname = args[0];
			inputDir = args[1];
		}
		
		OfflineInstrumenter instrumenter = new OfflineInstrumenter();
		
		
		instrumenter.addInputDirectory(new File(inputDir), new File(inputDir));
		
		instrumenter.beginTraversal();
//		PrintWriter writer = new PrintWriter(System.out);
		instrumenter.setPassUnmodifiedClasses(true);
		
		
		
		File path = new File(pathname);
		
		if (!path.exists()) {
			path.mkdirs();
		}
		
		ClassInstrumenter ci;
		while ((ci = instrumenter.nextClass()) != null) {			
			
			for (int i = 0; i < ci.getReader().getMethodCount(); i++) {
				
				MethodData methodData = ci.visitMethod(i);				
				
				String name = TestUtilities.createFileName(ci.getReader().getName(), methodData.getName());
				
				PrintWriter fileWriter = new PrintWriter(new FileOutputStream(pathname + File.separatorChar + name));
				
				LOG.info("writing bytecode for {}.{}", ci.getReader().getName(), methodData.getName());
				
				new Disassembler(methodData).disassembleTo(fileWriter);
				fileWriter.flush();
				fileWriter.close();
				
			}
			
		}
		
	}
}
