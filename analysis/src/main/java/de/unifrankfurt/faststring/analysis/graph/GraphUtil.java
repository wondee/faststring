package de.unifrankfurt.faststring.analysis.graph;

import com.google.common.base.Function;


public class GraphUtil {

	public static final Function<StringReference, Integer> stringReferenceToInt = new Function<StringReference, Integer>() {
		@Override
		public Integer apply(StringReference input) {
			return input.getRef();
		}
	};
	
//	static List<Integer> listOfValueNumbers(Set<PointerNode> nodes) {
//		return Lists.transform(Lists.newLinkedList(nodes), new Function<PointerNode, Integer>() {
//
//			@Override
//			public Integer apply(PointerNode input) {
//				return input.valueNumber();
//			}
//		});
//	}
//	
//	public static boolean hasEdge(PointerNode srcNode, PointerNode targetNode) {
//			
//		boolean hasSuccessor = srcNode.hasSuccessor(targetNode.valueNumber());
//		boolean hasPredecessor = targetNode.hasPredecessor(srcNode.valueNumber());
//		if (!hasSuccessor && !hasPredecessor) {
//			return false;
//		} 
//		
//		if (hasPredecessor && hasSuccessor) {
//			return true;
//		}
//		
//		throw new IllegalArgumentException("edge (" + srcNode.valueNumber() + ", " + targetNode.valueNumber() + 
//				") is not consistent: hasSucc is " + hasSuccessor + " hasPred is " + hasPredecessor );
//	
//	}
	
	
}
