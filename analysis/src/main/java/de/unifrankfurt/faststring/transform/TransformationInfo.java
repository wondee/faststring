package de.unifrankfurt.faststring.transform;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;
import com.ibm.wala.util.collections.Pair;

import de.unifrankfurt.faststring.analysis.AnalysisResult;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public class TransformationInfo {
	
	private Collection<Reference> references;

	private BiMap<Pair<TypeLabel, Integer>, Integer> locals;
	
	private String methodName;

	private Set<Integer> effectedVars;

	public TransformationInfo(AnalysisResult result) {
		methodName = result.getMethodName();
		references = result.getRefs();
		
		effectedVars = Sets.newHashSet();
		
		Set<TypeLabel> labels = Sets.newHashSet();
		
		for (Reference ref : result.getRefs()) {
			InstructionNode def = ref.getDefinition();
			
			for (Integer local : def.getLocals(ref.valueNumber())) {
				effectedVars.add(local);
			}

			for (InstructionNode use : ref.getUses()) {
				for (Integer local : use.getLocals(ref.valueNumber())) {
					effectedVars.add(local);
				}

			}
			
			labels.add(ref.getLabel());
		}

		locals = createOriginal2Optimized(labels, effectedVars, result.getMaxLocals());

	}
	
	private BiMap<Pair<TypeLabel, Integer>, Integer> createOriginal2Optimized(Set<TypeLabel> labels, Set<Integer> keySet, int maxLocals) {
		BiMap<Pair<TypeLabel, Integer>, Integer> map = HashBiMap.create(keySet.size());
		
		int optLocal = maxLocals + 1;
		
		for (TypeLabel label : labels) {
			
			for (Integer originalLocal : keySet) {
				map.put(Pair.make(label, originalLocal), optLocal);
				optLocal++;
			}
		}
		return map;
	}

	public Set<Integer> getEffectedVars() {
		return effectedVars;
	}

	public String getMethodName() {
		return methodName;
	}

	public Collection<Reference> getReferences() {
		return references;
	}

	public int getLocalForLabel(TypeLabel from, TypeLabel to, int local) {
		
		if (from == null) {
			return locals.get(Pair.make(to, local));
		} else {
			int orgLocal = locals.inverse().get(local).snd;
			
			if (to != null) {
				return locals.get(Pair.make(to, orgLocal));
			} else {
				return orgLocal;
			}
		}
	}

	public int getOrgLocalForLabel(TypeLabel label, int local) {
		return getLocalForLabel(label, null, local);
	}
}
