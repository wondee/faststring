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
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.unifrankfurt.faststring.analysis.AnalysisResult;
import de.unifrankfurt.faststring.analysis.graph.Reference;
import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.model.ConstantDefinition;
import de.unifrankfurt.faststring.analysis.model.DataFlowObject;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;
import de.unifrankfurt.faststring.analysis.util.GraphUtil;
import de.unifrankfurt.faststring.analysis.util.StringUtil;

public class TransformationInfo {

	private TypeLabel label;

	private Multimap<Integer, DataFlowObject> defMap;
	private Map<Integer, Constant> constMap;

	private ImmutableSet<Definition> defConversations;

	private BiMap<Integer, Integer> org2opt;

//	private Multimap<Integer, Use> bc2Use;

	public TransformationInfo(AnalysisResult result) {
		label = result.getLabel();

		Multimap<Integer, DataFlowObject> defMap = HashMultimap.create();
		Multimap<Integer, Use> bc2Use = HashMultimap.create();

		defConversations = ImmutableSet.copyOf(GraphUtil.extractDefConversions(result.getRefs()).values());


		Collection<Set<Use>> values = GraphUtil.extractUsageConversions(result.getRefs()).values();
		Set<Use> useConversations = Sets.newHashSet(Iterables.concat(values));

		for (Reference ref : result.getRefs()) {
			Definition def = ref.getDef();

			for (Integer id : def.getLocalVariableIndex()) {
				defMap.put(id, def);
			}


			for (Use use : ref.getUses()) {
				for (Integer id : use.getLocalVariableIndex()) {
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
			Definition def = ref.getDef();

			if (def instanceof ConstantDefinition) {
				map.put(ref.getRef(), new Constant((((ConstantDefinition)def).getValue()), maxLocals));
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

	private void init(Multimap<Integer, DataFlowObject> defMap) {
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

	public ImmutableSet<Definition> getDefinitionConversations() {
		return defConversations;
	}

	public int getOptLocal(Integer orgLocal) {
		return org2opt.get(orgLocal);
	}
}
