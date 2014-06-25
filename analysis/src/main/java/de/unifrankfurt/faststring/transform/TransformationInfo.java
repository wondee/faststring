package de.unifrankfurt.faststring.transform;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.unifrankfurt.faststring.analysis.AnalysisResult;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.model.DataFlowObject;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;

public class TransformationInfo {

	private Multimap<Integer, DataFlowObject> defMap;

	public TransformationInfo(AnalysisResult result) {
		Multimap<Integer, DataFlowObject> defMap = HashMultimap.create();
		
		Set<Definition> defConversations = Sets.newHashSet();
		
		for (Reference ref : result.getRefs()) {
			Definition def = ref.getDef();
			
			for (Integer id : def.getLocalVariableIndex()) {
				defMap.put(id, def);
			}
			
			
			if (ref.isDefinitionConversion()) {
				defConversations.add(def);
			}
			
			for (Use use : ref.getUses()) {
				for (Integer id : use.getLocalVariableIndex()) {
					defMap.put(id, use);
				}
				
			}
		}
		
		init(defMap);
	}

	private void init(Multimap<Integer, DataFlowObject> defMap) {
		this.defMap = ImmutableMultimap.copyOf(defMap);
		
	}

	public Set<Integer> getEffectedVars() {
		return defMap.keySet();
	}
	
}
