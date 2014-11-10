package de.unifrankfurt.faststring.analysis.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * provides some helper types and methods for working with the {@link UniqueQueue}
 *
 * @author markus
 *
 */
public final class QueueUtil {

	public static <T> void processUntilQueueIsEmpty(Collection<T> initialElems, QueueProcessingStrategy<T> strategy) {
		Queue<T> queue = strategy.initQueue(initialElems);

		while(strategy.canceled() || !queue.isEmpty()) {
			strategy.process(queue.remove(), queue);
		}

		strategy.finished();
	}


	public static <T> void processUntilQueueIsEmpty(Iterator<T> initialElems, QueueProcessingStrategy<T> strategy) {
		processUntilQueueIsEmpty(Lists.newArrayList(initialElems), strategy);
	}

	@SafeVarargs
	public static <T> void processUntilQueueIsEmpty(QueueProcessingStrategy<T> strategy, T ... initialElems) {
		processUntilQueueIsEmpty(Arrays.asList(initialElems), strategy);
	}

	public static interface QueueProcessingStrategy<T> {

		Queue<T> initQueue(Collection<T> initialElems);

		boolean canceled();

		void process(T t, Queue<T> queue);

		void finished();
	}

	public static abstract class BaseQueueProcessingStrategy<T> implements QueueProcessingStrategy<T> {
		@Override
		public Queue<T> initQueue(Collection<T> initialElems) {
			return new UniqueQueue<>(initialElems);
		}

		@Override
		public void finished() {
			// empty stub
		}

		@Override
		public boolean canceled() {
			return false;
		}

	}

	public static <T> void processUntilQueueIsEmpty(QueueProcessingStrategy<T> strategy) {
		processUntilQueueIsEmpty(ImmutableList.<T>of(), strategy);

	}


}
