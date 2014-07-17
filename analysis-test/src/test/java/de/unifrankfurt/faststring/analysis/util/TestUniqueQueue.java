package de.unifrankfurt.faststring.analysis.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TestUniqueQueue {

	
	
	@Test
	public void testPeek() throws Exception {
		UniqueQueue<Integer> filledQueue = new UniqueQueue<Integer>(Arrays.asList(2,3,4,5,6));
		UniqueQueue<Integer> emptyQueue = new UniqueQueue<Integer>(Lists.<Integer>newArrayList());

		assertEquals(2, (int) filledQueue.peek());
		assertNull(emptyQueue.peek());
		
		assertEquals(5, filledQueue.size());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testElementOnEmptyQueue() throws Exception {
		UniqueQueue<Integer> emptyQueue = new UniqueQueue<Integer>(Lists.<Integer>newArrayList());
		
		emptyQueue.element();
	}

	@Test
	public void testSize() throws Exception {
		UniqueQueue<Integer> filledQueue = new UniqueQueue<Integer>(Arrays.asList(2,3,4,5,6,5,3));
		UniqueQueue<Integer> emptyQueue = new UniqueQueue<Integer>(Lists.<Integer>newArrayList());
		
		assertEquals(5, filledQueue.size());
		assertEquals(0, emptyQueue.size());
		
		assertTrue(emptyQueue.isEmpty());
		assertFalse(filledQueue.isEmpty());
	}
	
	@Test
	public void testRemove() throws Exception {
		UniqueQueue<Integer> filledQueue = new UniqueQueue<Integer>(Arrays.asList(2,3,4,5,6));
		
		int initialSize = filledQueue.size();
		
		assertEquals(2, (int) filledQueue.remove());
		assertEquals(3, (int) filledQueue.remove());
		
		assertEquals(initialSize - 2, filledQueue.size());
		
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testRemoveOnEmpty() throws Exception {
		UniqueQueue<Integer> emptyQueue = new UniqueQueue<Integer>(Lists.<Integer>newArrayList());
		
		assertNull(emptyQueue.poll());
		
		emptyQueue.remove();
	}
	
	@Test
	public void testAdd() throws Exception {
		UniqueQueue<Integer> queue = new UniqueQueue<Integer>(Lists.<Integer>newArrayList());
		
		assertTrue(queue.add(1));
		assertTrue(queue.add(2));
		assertTrue(queue.add(3));
		assertFalse(queue.add(2));
		assertFalse(queue.add(3));
		
		assertEquals(3, queue.size());
		
		assertEquals(1, (int) queue.remove());
	}
	
}
