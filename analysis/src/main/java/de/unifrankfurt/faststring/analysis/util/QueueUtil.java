package de.unifrankfurt.faststring.analysis.util;

import java.util.Collection;
import java.util.Queue;

import com.google.common.collect.ImmutableList;

public final class QueueUtil {

	public static <T> void processUntilQueueIsEmpty(Collection<T> initialElems, QueueProcessingStrategy<T> strategy) {
		Queue<T> queue = strategy.initQueue(initialElems);
		
		while(!queue.isEmpty()) {
			strategy.process(queue.remove(), queue);
		}
		
		strategy.finished();
	}


	public static interface QueueProcessingStrategy<T> {
		
		Queue<T> initQueue(Collection<T> initialElems);
		
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
		
	}

	public static <T> void processUntilQueueIsEmpty(QueueProcessingStrategy<T> strategy) {
		processUntilQueueIsEmpty(ImmutableList.<T>of(), strategy);
		
	}
	
}
