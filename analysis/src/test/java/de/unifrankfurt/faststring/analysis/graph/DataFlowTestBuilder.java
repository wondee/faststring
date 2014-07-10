package de.unifrankfurt.faststring.analysis.graph;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.unifrankfurt.faststring.analysis.label.BuiltInTypes;
import de.unifrankfurt.faststring.analysis.label.SubstringStringType;

public class DataFlowTestBuilder {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataFlowTestBuilder.class);
	
	private Map<Integer, Reference> nodeMap;
	
	public DataFlowTestBuilder() {
		nodeMap = Maps.newHashMap();
	}
	
	public DataFlowTestBuilder phi(int def, Integer...uses) {
		List<Integer> usesList = Arrays.asList(uses);
		InstructionNode phi = new PhiNode(def, usesList);
		addDefinition(def, phi);
		
		for (Integer v : uses) {
			addUse(v, phi);
		}
		
		return this;
		
	}
	
	public DataFlowTestBuilder labelUse(int rec, int def) {
		InstructionNode call = new MethodCallInstruction(def, Arrays.asList(rec), SubstringStringType.METHOD_SUBSTRING, false);
				
//				Use.createUsedAsReceiver(SubstringStringType.METHOD_SUBSTRING, def, Collections.<Integer>emptyList());
		addUse(rec, call);
				
		addDefinition(def, call);
		
		nodeMap.get(rec).setLabel(BuiltInTypes.SUBSTRING);
		
		return this;
	}

	public DataFlowTestBuilder parameterDefinition(int i) {
		addDefinition(i, new ParameterNode(i));
		return this;
	}

	public DataFlowTestBuilder return_(int i) {
		addUse(i, new ReturnNode(i));
		return this;
	}
	
	private void addDefinition(int v, InstructionNode def) {
		ensureRef(v);
		Reference ref = nodeMap.get(v);
		
		if (ref.getDefinition() == null) {
			ref.setDefinition(def);
		} else {
			throw new IllegalStateException("definition already set " + ref);
		}
	}

	private void addUse(int v, InstructionNode use) {
		ensureRef(v);
		nodeMap.get(v).getUses().add(use);
	}

	private void ensureRef(Integer v) {
		if (!nodeMap.containsKey(v)) {
			nodeMap.put(v, new Reference(v));
			nodeMap.get(v).setUsesMutable(Lists.<InstructionNode>newLinkedList());
		}
	}
	
	public DataFlowGraph create() {
		DataFlowGraph graph = new DataFlowGraph(BuiltInTypes.SUBSTRING, nodeMap);
		LOG.debug("created test graph:  {}", graph);
		
		return graph;
	}

	
	
}
