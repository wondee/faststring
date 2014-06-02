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
import de.unifrankfurt.faststring.analysis.util.UniqueQueue;

public class SubstringAnalyzer {

	private static final Logger LOG = LoggerFactory.getLogger(SubstringAnalyzer.class);
	
	private TargetApplication targetApp;
	
	private IMethod method;

	private IR ir;
	private DefUse defUse;

	
	private StringCallIdentifier identifier = new StringCallIdentifier(Label.SUBSTRING);

	
	public SubstringAnalyzer(TargetApplication targetApplication, IMethod m) {
		method = m;
		targetApp = targetApplication;
		
		ir = targetApp.findIRForMethod(method);
		defUse = targetApp.findDefUseForMethod(method);	
	}
	
	
	public Set<StringReference> findCandidates() {
		LOG.info("analyzing Method: {}", method.getSignature());
		
		DataFlowGraph graph = new DataFlowGraphBuilder(identifier, ir, defUse).createDataFlowGraph();
		
		Collection<StringReference> substring = graph.getAllLabelMatchingReferences();
		
		Set<StringReference> bubble = Sets.newHashSet(substring);
		
		if (substring.size() > 0) {
			Queue<StringReference> refQueue = new UniqueQueue<>(substring);
			
			while(!refQueue.isEmpty()) {
				refQueue.remove();
				
				
			}
			
		} else {
			LOG.debug("no string uses found");
		}	
		return bubble;
		
	}
	





//	private UseRegister checkCandidates(Map<Integer, List<Integer>> stringCalls) {
//		
//		UseRegister register = new UseRegister();
//		IRAnalyzer analyzer = new IRAnalyzer(ir);
//		
//		for (Entry<Integer, List<Integer>> call : stringCalls.entrySet()) {
//			
//			Queue<Integer> vs = Queues.newArrayDeque(call.getValue());
//			
//			while (!vs.isEmpty()) {
//				Integer v = vs.remove();
//				Integer insIndex = call.getKey();
//				Iterator<SSAInstruction> uses = defUse.getUses(v);
//				while (uses.hasNext()) {
//					
//					SSAInstruction use = uses.next();
//					if (!(use instanceof SSAPhiInstruction)) {
//						boolean usedLater = analyzer.isConnected(insIndex, use);
//						
//						register.add(v, usedLater);
//					} else {
//						Boolean pointerShadow = register.getCandidate(use.getDef());
//						if (pointerShadow == null) {
//							vs.add(v);
//						} else {
//							register.add(v, pointerShadow);
//						}
//					}
//				}
//			}
//			
//		}
//		
//		return register;
//
//	}
	
}
