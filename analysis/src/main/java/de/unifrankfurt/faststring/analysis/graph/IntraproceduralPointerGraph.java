package de.unifrankfurt.faststring.analysis.graph;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import com.google.common.collect.Maps;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;

import de.unifrankfurt.faststring.analysis.model.StringReference;

public class IntraproceduralPointerGraph {
	
	Map<Integer, PointerNode> nodes = Maps.newHashMap();

	
	public boolean contains(Integer key) {
		return nodes.containsKey(key);
	}

	public PointerNode get(int ref) {
		return nodes.get(ref);
	}

	public void add(PointerNode node) {
		nodes.put(node.valueNumber(), node);
	}

	public void addEdge(int src, int target) {
		PointerNode srcNode = nodes.get(src);
		PointerNode targetNode = nodes.get(target);
		srcNode.addSuccessor(targetNode);
		targetNode.addPredecessor(srcNode);
		
	}

	public int size() {
		return nodes.size();
	}

	public static IntraproceduralPointerGraph create(DefUse defUse,
			Queue<StringReference> stringUses) {
		IntraproceduralPointerGraph graph = new IntraproceduralPointerGraph();
		
		while (!stringUses.isEmpty()) {
			StringReference stringUse = (StringReference) stringUses.remove();
			int ref = stringUse.ref();
			
			if (!graph.contains(stringUse.ref())) {
				graph.add(new PointerNode(stringUse.ref()));
			}
			
			SSAInstruction def = defUse.getDef(ref);
			
			if (def instanceof SSAPhiInstruction) {
				for (int i = 0; i < def.getNumberOfUses(); i++) {
					int pointer = def.getUse(i);
					
					addToGraph(stringUses, graph, pointer);
					graph.addEdge(pointer, ref);
					
				}
			}
			
			Iterator<SSAInstruction> uses = defUse.getUses(ref);
			
			while (uses.hasNext()) {
				SSAInstruction use = uses.next();
				if (use instanceof SSAPhiInstruction) {
					int pointer = use.getDef();
					
					addToGraph(stringUses, graph, pointer);
					graph.addEdge(ref, pointer);
					
				}
			}
		}
		
		return graph;
	}
	
	private static void addToGraph(Queue<StringReference> stringUses,
			IntraproceduralPointerGraph graph, int pointer) {
		if (!graph.contains(pointer)) {
			graph.add(new PointerNode(pointer));
			
			stringUses.add(new StringReference(pointer));
		}
	}
	
	
}