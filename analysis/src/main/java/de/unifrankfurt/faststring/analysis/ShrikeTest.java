package de.unifrankfurt.faststring.analysis;

import java.io.File;
import java.io.PrintStream;

import com.ibm.wala.shrikeBT.ConstantInstruction;
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
		
		ClassInstrumenter ci;
		
		final GetInstruction getOut = Util.makeGet(System.class, "out");
		final Instruction callPrintln = Util.makeInvoke(PrintStream.class, "println", new Class[] { String.class });

		instrumenter.beginTraversal();
		
		instrumenter.setOutputJar(new File("../analysis-test/target/helloWorld.jar"));
		
		while ((ci = instrumenter.nextClass()) != null) {
			
			for (int i = 0; i < ci.getReader().getMethodCount(); i++) {
				
				MethodData methodData = ci.visitMethod(i);
				System.out.println("editing: " + methodData.getName());
				
				
				if (ci.getMethodCode(i) != null) {
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
					editor.applyPatches();
					editor.endPass();
					new Verifier(methodData).verify();
					
//					ci.replaceMethod(i, methodData);
					if (ci.isChanged()) {
						
						
						instrumenter.outputModifiedClass(ci);
					} else {
						System.out.println("no changes");
					}
				} else {
					System.out.println("old code for " + i + " is null");
				}
			}
			
		}
		
	}
	
	public void test() {
		System.out.println("Hallo Welt");
	}
}
