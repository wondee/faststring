package de.unifrankfurt.faststring.analysis;

import java.util.Collection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.model.DataFlowObject;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;

/**
 * Represents a result of a analysis
 * 
 * @author markus
 *
 */
public class AnalysisResult  {

	private Collection<Reference> refs;

	private Multimap<Integer, DataFlowObject> varDefs;
	
	public AnalysisResult(Collection<Reference> refs) {
		
		Multimap<Integer, DataFlowObject> defMap = HashMultimap.create();
		
		for (Reference ref : refs) {
			Definition def = ref.getDef();
			
			for (Integer id : def.getLocalVariableIndex()) {
				defMap.put(id, def);
			}
			
			for (Use use : ref.getUses()) {
				for (Integer id : use.getLocalVariableIndex()) {
					defMap.put(id, use);
				}
				
			}
		}
		
		
		
		init(refs, defMap);
	}

	
	
	private void init(Collection<Reference> refs, Multimap<Integer, DataFlowObject> defMap) {
		this.refs = ImmutableSet.copyOf(refs);
		this.varDefs = ImmutableMultimap.copyOf(defMap);
		
	}

	public Collection<Reference> getRefs() {
		return refs;
	}

	public boolean isEmpty() {
		return refs.isEmpty();
	}
	
	public Collection<Integer> getEffectedVars() {
		return varDefs.keySet();
	}
}
