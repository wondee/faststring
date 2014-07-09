package de.unifrankfurt.faststring.transform;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.ibm.wala.util.collections.Pair;

import de.unifrankfurt.faststring.analysis.AnalysisResult;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;


public class TransformationInfo {

	private TypeLabel label;

	private Multimap<Integer, InstructionNode> defMap;

	private Collection<Reference> references;

	private BiMap<Pair<TypeLabel, Integer>, Integer> locals;
	
	private String methodName;

//	private Multimap<Integer, Use> bc2Use;

	public TransformationInfo(AnalysisResult result) {
		label = result.getLabel();
		methodName = result.getMethodName();
		references = result.getRefs();
		
		Multimap<Integer, InstructionNode> defMap = HashMultimap.create();
		Multimap<Integer, InstructionNode> bc2Use = HashMultimap.create();

//		Collection<Set<Use>> values = GraphUtil.extractUsageConversions(result.getRefs()).values();
//		Set<Use> useConversations = Sets.newHashSet(Iterables.concat(values));

		for (Reference ref : result.getRefs()) {
			InstructionNode def = ref.getDefinition();

			for (Integer id : def.getLocalVariableIndex(ref.valueNumber())) {
				defMap.put(id, def);
			}


			for (InstructionNode use : ref.getUses()) {
				for (Integer id : use.getLocalVariableIndex(ref.valueNumber())) {
					defMap.put(id, use);
				}

				int byteCodeIndex = use.getByteCodeIndex();
				if (byteCodeIndex != -1) {
					bc2Use.put(byteCodeIndex, use);
				}

			}
		}

		locals = createOriginal2Optimized(result.getLabel(), defMap.keySet(), result.getMaxLocals());


//		System.out.println("defConversations" + StringUtil.toStringWithLineBreak(defConversations));
//		System.out.println("useConversations" + StringUtil.toStringWithLineBreak(useConversations));
//		System.out.println("bcIndex" + StringUtil.toStringMap(bc2Use.asMap()));
//		System.out.println("org2opt" + StringUtil.toStringMap(org2opt));

		init(defMap);
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

	private void init(Multimap<Integer, InstructionNode> defMap) {
		this.defMap = ImmutableMultimap.copyOf(defMap);

	}

	public Set<Integer> getEffectedVars() {
		return defMap.keySet();
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
