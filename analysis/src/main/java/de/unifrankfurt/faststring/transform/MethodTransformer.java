package de.unifrankfurt.faststring.transform;

import java.util.Set;

import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.MethodEditor;
import com.ibm.wala.shrikeBT.analysis.Analyzer.FailureException;
import com.ibm.wala.shrikeBT.analysis.Verifier;

import de.unifrankfurt.faststring.analysis.graph.StringReference;
import de.unifrankfurt.faststring.analysis.model.Definition;

public class MethodTransformer {

	
	public void transformMethod(MethodData methodData, Set<StringReference> refs) {
		System.out.println("editing: " + methodData.getName());
		
		
		
		MethodEditor editor = new MethodEditor(methodData);
		editor.beginPass();
		
		for (StringReference ref : refs) {
			
			Definition def = ref.getDef();
			
			int index = def.getByteCodeIndex();
			if (index == -1) {
				System.out.println("no index");
			} else {
				editor.insertBefore(index, PatchFactory.createDefinitionPatch(ref));
			}
			
		}
		
//		editor.insertBefore(i, p);
		
		try {
			new Verifier(methodData).verify();
		} catch (FailureException e) {
			throw new IllegalStateException(e);
		}

		editor.applyPatches();
		editor.endPass();
		
	}
}
