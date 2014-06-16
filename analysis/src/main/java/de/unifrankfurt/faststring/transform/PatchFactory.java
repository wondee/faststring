package de.unifrankfurt.faststring.transform;

import com.ibm.wala.shrikeBT.LoadInstruction;
import com.ibm.wala.shrikeBT.MethodEditor.Output;
import com.ibm.wala.shrikeBT.MethodEditor.Patch;

import de.unifrankfurt.faststring.analysis.graph.Reference;

public class PatchFactory {

	public static Patch createDefinitionPatch(Reference ref) {
		
		return new Patch() {

			@Override
			public void emitTo(Output w) {
				LoadInstruction.make("",2);
				
			}
			
		};
	}

}
