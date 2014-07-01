package de.unifrankfurt.faststring.analysis.graph;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.unifrankfurt.faststring.analysis.label.BuiltInTypes;
import de.unifrankfurt.faststring.analysis.label.SubstringStringType;
import de.unifrankfurt.faststring.analysis.model.Definition;
import de.unifrankfurt.faststring.analysis.model.Use;

public class DataFlowTestBuilder {

	private Map<Integer, Reference> nodeMap;
	
	public DataFlowTestBuilder() {
		nodeMap = Maps.newHashMap();
	}
	
	public DataFlowTestBuilder addPhi(int def, Integer...uses) {
		List<Integer> usesList = Arrays.asList(uses);
		Definition phiDefinition = Definition.createPhiDefinition(usesList);
		addToMap(def, phiDefinition);
		
		for (Integer v : uses) {
			Use use = Use.createUsedInPhi(def, usesList);
			
			addToMap(v, use);
		}
		
		return this;
		
	}
	
	public DataFlowTestBuilder addLabelUse(int rec, int def) {
		Use use =  Use.createUsedAsReceiver(SubstringStringType.METHOD_SUBSTRING, def, Collections.<Integer>emptyList());
		addToMap(rec, use);
		
		nodeMap.get(rec).setLabel(BuiltInTypes.SUBSTRING);
		
		Definition definition = Definition.createCallResultDefinition(SubstringStringType.METHOD_SUBSTRING, rec);		
		addToMap(def, definition);
		
		return this;
	}

	public DataFlowTestBuilder addParameterDefinition(int i) {
		addToMap(i, Definition.createParamDefinition(i));
		return this;
	}

	
	private void addToMap(int v, Definition def) {
		ensureRef(v);
		Reference ref = nodeMap.get(v);
		
		if (ref.getDef() == null) {
			ref.setDefinition(def);
		} else {
			throw new IllegalStateException("definition already set " + ref);
		}
	}

	private void addToMap(int v, Use use) {
		ensureRef(v);
		nodeMap.get(v).getUses().add(use);
	}

	private void ensureRef(Integer v) {
		if (!nodeMap.containsKey(v)) {
			nodeMap.put(v, new Reference(v));
			nodeMap.get(v).setUsesMutable(Lists.<Use>newLinkedList());
		}
	}
	
	public DataFlowGraph create() {
		return new DataFlowGraph(BuiltInTypes.SUBSTRING, nodeMap);
	}
	
}
