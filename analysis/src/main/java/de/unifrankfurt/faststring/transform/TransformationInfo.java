package de.unifrankfurt.faststring.transform;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import de.unifrankfurt.faststring.analysis.AnalysisResult;
import de.unifrankfurt.faststring.analysis.graph.ConstantDefinition;
import de.unifrankfurt.faststring.analysis.graph.InstructionNode;
import de.unifrankfurt.faststring.analysis.graph.Labelable;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.util.GraphUtil;


public class TransformationInfo {

	private TypeLabel label;

	private Multimap<Integer, InstructionNode> defMap;
	private Map<Integer, Constant> constMap;

	private ImmutableSet<Reference> defConversations;

	private BiMap<Integer, Integer> org2opt;

	private String methodName;

//	private Multimap<Integer, Use> bc2Use;

	public TransformationInfo(AnalysisResult result) {
		label = result.getLabel();
		methodName = result.getMethodName();

		Multimap<Integer, InstructionNode> defMap = HashMultimap.create();
		Multimap<Integer, InstructionNode> bc2Use = HashMultimap.create();

		defConversations = ImmutableSet.copyOf(GraphUtil.extractReferencesWithDefConversionsToOpt(result.getRefs(), label));


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


		int maxLocals = result.getMaxLocals();
		constMap = createConstant2Optimized(result, maxLocals);
		org2opt = createOriginal2Optimized(defMap.keySet(), maxLocals + constMap.size());


//		System.out.println("defConversations" + StringUtil.toStringWithLineBreak(defConversations));
//		System.out.println("useConversations" + StringUtil.toStringWithLineBreak(useConversations));
//		System.out.println("bcIndex" + StringUtil.toStringMap(bc2Use.asMap()));
//		System.out.println("org2opt" + StringUtil.toStringMap(org2opt));

		init(defMap);
	}

	private Map<Integer, Constant> createConstant2Optimized(AnalysisResult result, int maxLocals) {
		Map<Integer, Constant> map = Maps.newHashMap();


		for (Reference ref : result.getRefs()) {
			Labelable def = ref.getDefinition();

			if (def instanceof ConstantDefinition) {
				map.put(ref.valueNumber(), new Constant((((ConstantDefinition)def).getValue()), maxLocals));
			}

			maxLocals++;
		}

		return ImmutableMap.copyOf(map);
	}

	public class Constant {
		private Object value;

		private int local;

		Constant(Object value, int local) {
			this.value = value;
			this.local = local;
		}

		public Object getValue() {
			return value;
		}

		public int getLocal() {
			return local;
		}

	}

	private BiMap<Integer, Integer> createOriginal2Optimized(Set<Integer> keySet, int maxLocals) {
		BiMap<Integer, Integer> map = HashBiMap.create(keySet.size());

		int optLocal = maxLocals + 1;

		for (Integer originalLocal : keySet) {
			map.put(originalLocal, optLocal);
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

	public Collection<Constant> getConstants() {
		return constMap.values();

	}

	public TypeLabel getLabel() {
		return label;
	}

	public Set<Reference> getDefinitionConversationsToOpt() {
		return defConversations;
	}

	public int getOptLocal(Integer orgLocal) {
		return org2opt.get(orgLocal);
	}

	public String getMethodName() {
		return methodName;
	}
}
