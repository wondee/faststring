package de.unifrankfurt.faststring.analysis.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;

import de.unifrankfurt.faststring.analysis.model.StringReference;

public class GraphBuilder {

	private static final Logger LOG = LoggerFactory
			.getLogger(GraphBuilder.class);

	public static IntraproceduralPointerGraph create(DefUse defUse,
			Queue<StringReference> stringUses) {
		IntraproceduralPointerGraphModel graphModel = new IntraproceduralPointerGraphModel();
		
		while (!stringUses.isEmpty()) {
			StringReference stringUse = (StringReference) stringUses.remove();
			int ref = stringUse.valueNumber();
			
			if (!graphModel.contains(stringUse.valueNumber())) {
				graphModel.add(new PointerNodeModel(stringUse.valueNumber()));
			}
			
			SSAInstruction def = defUse.getDef(ref);
			
			if (def instanceof SSAPhiInstruction) {
				for (int i = 0; i < def.getNumberOfUses(); i++) {
					int pointer = def.getUse(i);
					
					addToGraph(stringUses, graphModel, pointer);
					graphModel.addEdge(pointer, ref);
					
				}
			}
			
			Iterator<SSAInstruction> uses = defUse.getUses(ref);
			
			while (uses.hasNext()) {
				SSAInstruction use = uses.next();
				if (use instanceof SSAPhiInstruction) {
					int pointer = use.getDef();
					
					addToGraph(stringUses, graphModel, pointer);
					graphModel.addEdge(ref, pointer);
					
				}
			}
		}
		IntraproceduralPointerGraph graph = graphModel.build();
		LOG.debug("created graph: {}", graph);
		
		return graph;
	}
	
	private static void addToGraph(Queue<StringReference> stringUses,
			IntraproceduralPointerGraphModel graph, int pointer) {
		if (!graph.contains(pointer)) {
			graph.add(new PointerNodeModel(pointer));
			
			stringUses.add(new StringReference(pointer));
		}
	}
	
	private static class IntraproceduralPointerGraphModel {
		
		private Map<Integer, PointerNodeModel> nodes = Maps.newHashMap();
		
		public boolean contains(int valueNumber) {
			return nodes.containsKey(valueNumber);
		}

		public IntraproceduralPointerGraph build() {
			
			Map<Integer, PointerNode> nodeMap = Maps.transformValues(nodes, new Function<PointerNodeModel, PointerNode>() {

				@Override
				public PointerNode apply(PointerNodeModel model) {
					return model.build();
				}
			});
			
			Map<Integer, PointerNode> immutableNodes = new ImmutableMap.Builder<Integer, PointerNode>().putAll(nodeMap).build();
			
			return new IntraproceduralPointerGraph(immutableNodes);
		}

		public void addEdge(int src, int target) {
			PointerNodeModel srcNode = nodes.get(src);
			PointerNodeModel targetNode = nodes.get(target);

			if (!GraphUtil.hasEdge(srcNode, targetNode)) {
				srcNode.addSuccessor(targetNode.valueNumber());
				targetNode.addPredecessor(srcNode.valueNumber());
				//System.out.println("adding edge (" + src + ", " + target + ")");
			} else {
				//System.out.println("edge (" + src + ", " + target + ") already exists");
			}
		}

		public void add(PointerNodeModel pointerNode) {
			nodes.put(pointerNode.valueNumber, pointerNode);
			
		}
	}
	
	private static class PointerNodeModel implements PointerNode {

		private int valueNumber;

		private Set<Integer> succNodes = Sets.newHashSet();
		private Set<Integer> predNodes = Sets.newHashSet();
		
		
		public PointerNodeModel(int valueNumber) {
			this.valueNumber = valueNumber;
		}

		public PointerNodeImpl build() {
			
			Set<Integer> immutableSuccNodes = new ImmutableSet.Builder<Integer>().addAll(succNodes).build();
			Set<Integer> immutablePredNodes = new ImmutableSet.Builder<Integer>().addAll(predNodes).build();
			
			return new PointerNodeImpl(valueNumber, immutableSuccNodes, immutablePredNodes);
		}

		public void addSuccessor(Integer node) {
			succNodes.add(node);
		}

		public void addPredecessor(Integer node) {
			predNodes.add(node);
		}

		@Override
		public Integer valueNumber() {
			return valueNumber;
		}

		@Override
		public boolean hasSuccessor(Integer node) {
			return succNodes.contains(node);
		}

		@Override
		public boolean hasPredecessor(Integer node) {
			return predNodes.contains(node);
		}

		@Override
		public Collection<Integer> getPredeseccors() {
			throw new UnsupportedOperationException();
		}
		
	}
}
