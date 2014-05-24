package de.unifrankfurt.faststring.analysis.wala;

import static de.unifrankfurt.faststring.analysis.wala.IRUtil.STRING_TYPE;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

public class MethodAnalyzer {

	private static final Logger LOG = LoggerFactory.getLogger(MethodAnalyzer.class);
	
	private TargetApplication targetApp;
	
	private IMethod method;

	private IR ir;

	private DefUse defUse;

	
	public MethodAnalyzer(TargetApplication targetApplication, IMethod m) {
		method = m;
		targetApp = targetApplication;
		
		ir = targetApp.findIRForMethod(method);
		defUse = targetApp.findDefUseForMethod(method);
	}
	
	
	public Set<Integer> findCandidates() {
		LOG.info("analyzing Method: {}", method.getSignature());
		
		// get all references that are called with substring
		Queue<Use> stringUses = findStringsUses();
		
		Map<Integer, List<Integer>> stringCalls = checkForPhis(stringUses);
		
		UseRegister substringReceiver = checkCandidates(stringCalls);
		
		// return the SSA variable names for this method
		Set<Integer> candidates = substringReceiver.getCandidates();
		LOG.debug("returning candidates: {}", candidates);	    			
		
//		System.out.println("parameters: " + Arrays.toString(ir.getParameterValueNumbers()));
		
		
		return candidates;
		
	}


	private Map<Integer, List<Integer>> checkForPhis(Queue<Use> stringUses) {
		Map<Integer, List<Integer>> stringCalls = Maps.newHashMap();
		
		Queue<Use> q = Queues.newArrayDeque(stringUses);
		
		for (Use use : stringUses) {
			stringCalls.put(use.insIndex, Lists.newArrayList(use.ref));
		}
		
		while (!q.isEmpty()) {
			Use use = q.remove();
			
			SSAInstruction def = defUse.getDef(use.ref);
			
			if (def instanceof SSAPhiInstruction) {
				for (int i = 0; i < def.getNumberOfUses(); i++) {
					int pointerShadow = def.getUse(i);
					int insIndex = use.insIndex;
					List<Integer> pointers = stringCalls.get(insIndex);
					if (!pointers.contains(pointerShadow)) {
						pointers.add(pointerShadow);
						q.add(new Use(pointerShadow, insIndex));
					}
				}
				
			}
			
			Iterator<SSAInstruction> usesIter = defUse.getUses(use.ref);
			
			while (usesIter.hasNext()) {
				SSAInstruction ins = usesIter.next();
				if (ins instanceof SSAPhiInstruction) {
					int pointerShadow = ins.getDef();
					int insIndex = use.insIndex;
					List<Integer> pointers = stringCalls.get(insIndex);
					if (!pointers.contains(pointerShadow)) {
						pointers.add(pointerShadow);
						q.add(new Use(pointerShadow, insIndex));
					}
				}
				
			}
			
			
		}
	
		return stringCalls;
	}

	private UseRegister checkCandidates(Map<Integer, List<Integer>> stringCalls) {
		
		UseRegister register = new UseRegister();
		IRAnalyzer analyzer = new IRAnalyzer(ir);
		
		for (Entry<Integer, List<Integer>> call : stringCalls.entrySet()) {
			
			Queue<Integer> vs = Queues.newArrayDeque(call.getValue());
			
			while (!vs.isEmpty()) {
				Integer v = vs.remove();
				Integer insIndex = call.getKey();
				Iterator<SSAInstruction> uses = defUse.getUses(v);
				while (uses.hasNext()) {
					
					SSAInstruction use = uses.next();
					if (!(use instanceof SSAPhiInstruction)) {
						boolean usedLater = analyzer.isConnected(insIndex, use);
						
						register.add(v, usedLater);
					} else {
						Boolean pointerShadow = register.getCandidate(use.getDef());
						if (pointerShadow == null) {
							vs.add(v);
						} else {
							register.add(v, pointerShadow);
						}
					}
				}
			}
			
		}
		
		return register;

	}


	private Queue<Use> findStringsUses() {
		Queue<Use> stringUses = Queues.newArrayDeque();
		
		for (int i = 0; i < ir.getInstructions().length; i++) {
			
			// get the i'th instruction
			SSAInstruction ins = ir.getInstructions()[i];
			// some instructions are null... don't know why
			if (ins != null) {
				// if this is an invocation
				if (ins instanceof SSAAbstractInvokeInstruction) {
					SSAAbstractInvokeInstruction invokeIns = (SSAAbstractInvokeInstruction) ins;
					MethodReference target = invokeIns.getDeclaredTarget();
					TypeReference targetType = target.getDeclaringClass();
					// and the target object is a String and no static call
					if (targetType.equals(STRING_TYPE) && !invokeIns.isStatic()) {
						
						// get the method name and the receivers index
						// TODO: distinguish which method is actually called
						int receiver = invokeIns.getReceiver();
						LOG.debug("instruction id {} : methodName: {}; called on v{}", i, target.getName(), receiver);																		
						
						stringUses.add(new Use(receiver, i));
						
					}
				}
			}
				
		}
		return stringUses;
	}

	private class Use {
		
		int ref;
		int insIndex;
		
		Use(int receiver, int insIndex) {
			super();
			this.ref = receiver;
			this.insIndex = insIndex;
		}

		
		@Override
		public String toString() {
			return "Use [receiver=" + ref + ", insIndex=" + insIndex + "]";
		}
		
	}
	
}
