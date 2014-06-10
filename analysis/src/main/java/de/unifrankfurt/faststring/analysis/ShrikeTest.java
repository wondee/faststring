package de.unifrankfurt.faststring.analysis;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;

import com.ibm.wala.shrikeBT.ConstantInstruction;
import com.ibm.wala.shrikeBT.Disassembler;
import com.ibm.wala.shrikeBT.GetInstruction;
import com.ibm.wala.shrikeBT.Instruction;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.MethodEditor;
import com.ibm.wala.shrikeBT.MethodEditor.Output;
import com.ibm.wala.shrikeBT.Util;
import com.ibm.wala.shrikeBT.analysis.Verifier;
import com.ibm.wala.shrikeBT.shrikeCT.ClassInstrumenter;
import com.ibm.wala.shrikeBT.shrikeCT.OfflineInstrumenter;

public class ShrikeTest {

	/*
	 * Verifier 									// for typechecking
	 * ClassInstrumenter.createEmptyMethodData		// for creating new methods
	 * 
	 * 
	 */
	
	public static void main(String[] args) throws Exception {
		OfflineInstrumenter instrumenter = new OfflineInstrumenter();
		
		instrumenter.addInputJar(new File("../analysis-test/target/test.jar"));
		
		
		
		final GetInstruction getOut = Util.makeGet(System.class, "out");
		final Instruction callPrintln = Util.makeInvoke(PrintStream.class, "println", new Class[] { String.class });

		instrumenter.beginTraversal();
		
		instrumenter.setOutputJar(new File("../analysis-test/target/helloWorld.jar"));
		
		PrintWriter writer = new PrintWriter(System.out);
		instrumenter.setPassUnmodifiedClasses(true);
		
		ClassInstrumenter ci;
		while ((ci = instrumenter.nextClass()) != null) {
			
			for (int i = 0; i < ci.getReader().getMethodCount(); i++) {
				
				MethodData methodData = ci.visitMethod(i);
				System.out.println("editing: " + methodData.getName());
				
				
				if (ci.getMethodCode(i) != null) {
					System.out.println("old");
					new Disassembler(methodData).disassembleTo(writer);
					writer.flush();
					MethodEditor editor = new MethodEditor(methodData);
					editor.beginPass();
					editor.insertAtStart(new MethodEditor.Patch() {
						
						@Override
						public void emitTo(Output w) {
							w.emit(getOut);
							w.emit(ConstantInstruction.makeString("Hallo Welt!"));
							w.emit(callPrintln);
						}
					});
					new Verifier(methodData).verify();

					editor.applyPatches();
					editor.endPass();
					
					System.out.println("new");
					new Disassembler(methodData).disassembleTo(writer);
					writer.flush();
//					ci.replaceMethod(i, methodData);
					
					instrumenter.outputModifiedClass(ci, ci.emitClass());
					
				} else {
					System.out.println("old code for " + i + " is null");
				}
			}
			
		}
		
		instrumenter.close();
	}
	
	public void test() {
		System.out.println("Hallo Welt");
	}
}
