package de.unifrankfurt.faststring.analysis;

import static de.unifrankfurt.faststring.analysis.graph.GraphUtil.*;
import static de.unifrankfurt.faststring.analysis.IRUtil.METHOD_SUBSTRING;
import static de.unifrankfurt.faststring.analysis.IRUtil.METHOD_SUBSTRING_DEFAULT_START;
import static de.unifrankfurt.faststring.analysis.IRUtil.STRING_TYPE;

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
import com.google.common.collect.Sets;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;

import de.unifrankfurt.faststring.analysis.graph.GraphBuilder;
import de.unifrankfurt.faststring.analysis.graph.GraphUtil;
import de.unifrankfurt.faststring.analysis.graph.IntraproceduralPointerGraph;
import de.unifrankfurt.faststring.analysis.model.StringReference;
import de.unifrankfurt.faststring.analysis.model.StringUse;

public class SubstringAnalyzer {

	private static final Logger LOG = LoggerFactory.getLogger(SubstringAnalyzer.class);
	
	private TargetApplication targetApp;
	
	private IMethod method;

	private IR ir;
	private DefUse defUse;

	private Set<Integer> params;
	
	private StringCallIdentifier identifier = new StringCallIdentifier(METHOD_SUBSTRING, METHOD_SUBSTRING_DEFAULT_START);

	
	public SubstringAnalyzer(TargetApplication targetApplication, IMethod m) {
		method = m;
		targetApp = targetApplication;
		
		ir = targetApp.findIRForMethod(method);
		defUse = targetApp.findDefUseForMethod(method);
		
		params = Sets.newHashSet();
		
		for (Integer p : ir.getParameterValueNumbers()) {
			if (ir.getParameterType(p).equals(STRING_TYPE)) {
				params.add(p);
			}
		}		
	}
	
	
	public Set<Integer> findCandidates() {
		LOG.info("analyzing Method: {}", method.getSignature());
		
		// get all references that are called with substring
		Queue<StringReference> stringUses = findStringsUses();
		
		checkDefs(stringUses);
		
		IntraproceduralPointerGraph graph = GraphBuilder.create(defUse, stringUses);
		
		checkDefinition(stringUses, graph);
//		Map<Integer, List<Integer>> stringCalls = checkForPhis(stringUses);
		
//		UseRegister substringReceiver = checkCandidates(stringCalls);
//		
//		// return the SSA variable names for this method
//		Set<Integer> candidates = substringReceiver.getCandidates();
//		LOG.debug("returning candidates: {}", candidates);	    					
		
		return null;
		
	}
	


	private void checkDefinition(Queue<StringReference> stringUses,
			IntraproceduralPointerGraph graph) {
		for (StringReference ref : stringUses) {
			List<Integer> pointers = graph.findAllPredeseccors(ref.valueNumber());
			
		}
		
		
	}


	private void checkDefs(Queue<StringReference> stringUses) {
		for (StringReference stringUse : stringUses) {
			
			if (params.contains(stringUse.valueNumber())) {
				stringUse.setToParam();
			}
			// TODO: some possible other definitions
		}
		
	}


	private Queue<StringReference> findStringsUses() {
		Queue<StringReference> stringUses = Queues.newArrayDeque();
		
		for (int i = 0; i < ir.getInstructions().length; i++) {
			
			// get the i'th instruction
			SSAInstruction ins = ir.getInstructions()[i];
			
			int receiver = identifier.check(ins);
			
			if (receiver > -1) {
				stringUses.add(new StringUse(receiver, i));
				
			}
							
		}
		return stringUses;
	}

	
	private Map<Integer, List<Integer>> checkForPhis(Queue<StringUse> stringUses) {
		Map<Integer, List<Integer>> stringCalls = Maps.newHashMap();
		
		Queue<StringUse> q = Queues.newArrayDeque(stringUses);
		
		for (StringUse use : stringUses) {
			stringCalls.put(use.insIndex(), Lists.newArrayList(use.valueNumber()));
		}
		
		while (!q.isEmpty()) {
			StringUse use = q.remove();
			
			SSAInstruction def = defUse.getDef(use.valueNumber());
			
			if (def instanceof SSAPhiInstruction) {
				for (int i = 0; i < def.getNumberOfUses(); i++) {
					int pointerShadow = def.getUse(i);
					int insIndex = use.insIndex();
					List<Integer> pointers = stringCalls.get(insIndex);
					if (!pointers.contains(pointerShadow)) {
						pointers.add(pointerShadow);
						q.add(new StringUse(pointerShadow, insIndex));
					}
				}
				
			}
			
			Iterator<SSAInstruction> usesIter = defUse.getUses(use.valueNumber());
			
			while (usesIter.hasNext()) {
				SSAInstruction ins = usesIter.next();
				if (ins instanceof SSAPhiInstruction) {
					int pointerShadow = ins.getDef();
					int insIndex = use.insIndex();
					List<Integer> pointers = stringCalls.get(insIndex);
					if (!pointers.contains(pointerShadow)) {
						pointers.add(pointerShadow);
						q.add(new StringUse(pointerShadow, insIndex));
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
	
}
