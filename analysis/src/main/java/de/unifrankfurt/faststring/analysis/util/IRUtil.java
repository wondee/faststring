package de.unifrankfurt.faststring.analysis.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;

import de.unifrankfurt.faststring.analysis.util.QueueUtil.ProcessingStrategy;

public final class IRUtil {

	private IRUtil() {
		// empty
	}

	/**
	 * creates a {@link List} out of all uses found in the given {@link SSAInstruction} starting with the use of index
	 * {@code startsWith}.
	 *
	 * @param ins the {@code SSAInstruction} to look for uses
	 * @param startWith the index of the use to start with adding to the list
	 * @return a list containing the found uses
	 */
	public static List<Integer> getUsesList(SSAInstruction ins, int startWith) {
		int size = ins.getNumberOfUses();

		List<Integer> list = Lists.newArrayListWithExpectedSize(size);

		for (int i = startWith; i < size; i++) {
			list.add(ins.getUse(i));
		}
		return list;

	}

	public static List<Integer> getUses(SSAInstruction ins) {
		return getUsesList(ins, 0);
	}

	public static int findUseIndex(int v, SSAInvokeInstruction invoke) {
		return findUseIndex(v, invoke, 1);
	}

	public static int findUseIndex(int v, SSAInvokeInstruction invoke, int startWith) {
		// start with 1 because 0 is the receiver
		for (int i = startWith; i < invoke.getNumberOfUses(); i++) {
			if (invoke.getUse(i) == v) {
				return i;
			}
		}
		throw new IllegalStateException("value number not found in uses list");
	}

	public static Map<SSAInstruction, Integer> createInstructionToIndexMap(IR ir) {
		Map<SSAInstruction, Integer> map = Maps.newHashMap();

		for (int i = 0; i < ir.getInstructions().length; i++) {
			SSAInstruction instruction = ir.getInstructions()[i];

			if (instruction != null) {
				Integer old = map.put(instruction, i);
				if (old != null) {
					throw new IllegalStateException("instruction was set before: actual index = " + i +
							"; old index = " + old + "; instruction = " + instruction);
				}
			}
		}

		return map;
	}

	public static Set<Integer> findAllDefPointersFor(DefUse defUse, int valueNumber) {
		return findPointersFor(new PhiPointerDefProcessingStrategy(defUse, valueNumber));
	}

	public static Set<Integer> findAllUsesPointersFor(DefUse defUse, int valueNumber) {
		return findPointersFor(new PhiPointerUsesProcessingStrategy(defUse, valueNumber));
	}

	private static Set<Integer> findPointersFor(PhiPointerProcessingStrategy strategy) {

		QueueUtil.processUntilQueueIsEmpty(strategy);
		return strategy.getPointers();
	}

	private static abstract class PhiPointerProcessingStrategy implements ProcessingStrategy<Integer> {

		private Set<Integer> pointers = Sets.newHashSet();
		DefUse defUse;
		private int valueNumber;

		public PhiPointerProcessingStrategy(DefUse defUse, int valueNumber) {
			this.defUse = defUse;
			this.valueNumber = valueNumber;
		}

		protected abstract void fillQueue(Integer i, Queue<Integer> queue);

		@Override
		public Queue<Integer> initQueue(Collection<Integer> initialElems) {
			return new UniqueQueue<Integer>(Lists.newArrayList(valueNumber));
		}

		@Override
		public void process(Integer i, Queue<Integer> queue) {
			if (!pointers.contains(i)) {
				pointers.add(i);

				fillQueue(i, queue);
			}

		}

		public Set<Integer> getPointers() {
			return pointers;
		}


	}

	private static final class PhiPointerDefProcessingStrategy extends PhiPointerProcessingStrategy {

		public PhiPointerDefProcessingStrategy(DefUse defUse, int valueNumber) {
			super(defUse, valueNumber);
		}


		@Override
		protected void fillQueue(Integer i, Queue<Integer> queue) {
			SSAInstruction def = defUse.getDef(i);

			if (def instanceof SSAPhiInstruction) {
				List<Integer> uses = getUses(def);

				queue.addAll(uses);
			}
		}

	}

	private static final class PhiPointerUsesProcessingStrategy extends PhiPointerProcessingStrategy {

		public PhiPointerUsesProcessingStrategy(DefUse defUse, int valueNumber) {
			super(defUse, valueNumber);
		}


		@Override
		protected void fillQueue(Integer i, Queue<Integer> queue) {
			Iterator<SSAInstruction> uses = defUse.getUses(i);

			while(uses.hasNext()) {
				SSAInstruction use = uses.next();

				if (use instanceof SSAPhiInstruction) {
					queue.add(use.getDef());
				}
			}
		}

	}
}
