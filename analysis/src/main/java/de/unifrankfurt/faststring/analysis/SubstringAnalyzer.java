package de.unifrankfurt.faststring.analysis;

import java.util.Collection;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;

import de.unifrankfurt.faststring.analysis.graph.DataFlowGraph;
import de.unifrankfurt.faststring.analysis.graph.DataFlowGraphBuilder;
import de.unifrankfurt.faststring.analysis.graph.StringReference;
import de.unifrankfurt.faststring.analysis.label.Label;
import de.unifrankfurt.faststring.analysis.model.DataFlowObject;
import de.unifrankfurt.faststring.analysis.model.Use;
import de.unifrankfurt.faststring.analysis.util.UniqueQueue;

public class SubstringAnalyzer {

	private static final Logger LOG = LoggerFactory.getLogger(SubstringAnalyzer.class);
	
	private TargetApplication targetApp;
	
	private IMethod method;

	private IR ir;
	private DefUse defUse;

	private StringCallIdentifier identifier = new StringCallIdentifier(Label.SUBSTRING);

	private DataFlowGraph graph;

	
	public SubstringAnalyzer(TargetApplication targetApplication, IMethod m) {
		method = m;
		targetApp = targetApplication;
		
		ir = targetApp.findIRForMethod(method);
		defUse = targetApp.findDefUseForMethod(method);	
		
		graph = new DataFlowGraphBuilder(identifier, ir, defUse).createDataFlowGraph();
	}
	
	
	public Set<StringReference> findCandidates() {
		LOG.info("analyzing Method: {}", method.getSignature());
		
		Collection<StringReference> refs = graph.getAllLabelMatchingReferences();
		
		if (refs.size() > 0) {
			processUntilQueueIsEmpty(refs, definitionCheck);
			processUntilQueueIsEmpty(refs, usageCheck);
			
			
		} else {
			LOG.debug("no string uses found");
		}	
		return Sets.newHashSet(graph.getAllLabelMatchingReferences());
		
	}


	private void processUntilQueueIsEmpty(Collection<StringReference> contents, CheckingStrategy strategy) {
		Queue<StringReference> refQueue = new UniqueQueue<>(contents);
		
		while(!refQueue.isEmpty()) {

			strategy.checkReference(refQueue.remove(), refQueue);
		}
		
	}

	interface CheckingStrategy {
		void checkReference(StringReference ref, Queue<StringReference> refQueue);
	}
	
	CheckingStrategy definitionCheck = new CheckingStrategy() {
		@Override
		public void checkReference(StringReference ref, Queue<StringReference> refQueue) {
			check(ref.getDef(), refQueue);
		}
	};
	
	CheckingStrategy usageCheck = new CheckingStrategy() {
		@Override
		public void checkReference(StringReference ref, Queue<StringReference> refQueue) {
			for (Use use : ref.getUses()) {
				if (use.isCompatibleWith(identifier.label())) {
					System.out.println("compatible: " + use);
					check(use, refQueue);
				} else {
					System.out.println("not compatible: " + use);
				}
			}
		}
	};
	
	

	private void check(DataFlowObject o, Queue<StringReference> refQueue) {
		LOG.debug("checking {}", o);
		
		for (Integer connectedRefId : o.getConnectedRefs()) {
			StringReference connectedRef = graph.get(connectedRefId);
			
			LOG.debug("get connected {}", connectedRefId);
			
			if (connectedRef.getLabel() == null) {
				connectedRef.setLabel(identifier.label());
				
				refQueue.add(connectedRef);
			}
		}
		
	}
	
	
}
