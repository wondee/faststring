package de.unifrankfurt.faststring.analysis.util;

import java.util.Collection;
import java.util.Queue;

import com.google.common.collect.ImmutableList;

public final class QueueUtil {

	public static <T> void processUntilQueueIsEmpty(Collection<T> initialElems, ProcessingStrategy<T> strategy) {
		Queue<T> queue = strategy.initQueue(initialElems);
		
		while(!queue.isEmpty()) {
			strategy.process(queue.remove(), queue);
		}
		
	}


	public static interface ProcessingStrategy<T> {
		Queue<T> initQueue(Collection<T> initialElems);
		
		void process(T t, Queue<T> queue);
	}
	
	public static abstract class BaseProcessingStrategy<T> implements ProcessingStrategy<T> {
		@Override
		public Queue<T> initQueue(Collection<T> initialElems) {
			return new UniqueQueue<>(initialElems);
		}
		
	}

	public static <T> void processUntilQueueIsEmpty(ProcessingStrategy<T> strategy) {
		processUntilQueueIsEmpty(ImmutableList.<T>of(), strategy);
		
	}
	
}
