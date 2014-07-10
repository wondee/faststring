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

	private TypeLabel label;
	
	private Collection<Reference> references;

	private BiMap<Pair<TypeLabel, Integer>, Integer> locals;
	
	private String methodName;

	private Set<Integer> effectedVars;

//	private Multimap<Integer, Use> bc2Use;

	public TransformationInfo(AnalysisResult result) {
		label = result.getLabel();
		methodName = result.getMethodName();
		references = result.getRefs();
		
		effectedVars = Sets.newHashSet();
		
		for (Reference ref : result.getRefs()) {
			InstructionNode def = ref.getDefinition();
			
			for (Integer local : def.getLocalVariableIndex(ref.valueNumber())) {
				effectedVars.add(local);
			}

			for (InstructionNode use : ref.getUses()) {
				for (Integer local : use.getLocalVariableIndex(ref.valueNumber())) {
					effectedVars.add(local);
				}

			}
		}

		locals = createOriginal2Optimized(result.getLabel(), effectedVars, result.getMaxLocals());


//		System.out.println("defConversations" + StringUtil.toStringWithLineBreak(defConversations));
//		System.out.println("useConversations" + StringUtil.toStringWithLineBreak(useConversations));
//		System.out.println("bcIndex" + StringUtil.toStringMap(bc2Use.asMap()));
//		System.out.println("org2opt" + StringUtil.toStringMap(org2opt));

	}
	
	private BiMap<Pair<TypeLabel, Integer>, Integer> createOriginal2Optimized(TypeLabel label, Set<Integer> keySet, int maxLocals) {
		BiMap<Pair<TypeLabel, Integer>, Integer> map = HashBiMap.create(keySet.size());

		int optLocal = maxLocals + 1;

		for (Integer originalLocal : keySet) {
			map.put(Pair.make(label, originalLocal), optLocal);
			optLocal++;
		}

		return map;
	}

	public Set<Integer> getEffectedVars() {
		return effectedVars;
	}

	public TypeLabel getLabel() {
		return label;
	}

	public String getMethodName() {
		return methodName;
	}

	public Collection<Reference> getReferences() {
		return references;
	}

	public int getLocalForLabel(TypeLabel from, TypeLabel to, int local) {
		
		int orgLocal = (from != null) ? 
				locals.inverse().get(local).snd : 
				local;
				
		Integer integer = locals.get(Pair.make(to, orgLocal));
		
		if (integer == null) {
			throw new IllegalStateException(String.format("no local found for: local %d from %s to %s in %s", local, from, to, locals));
		}
		
		return integer;
	}
}
