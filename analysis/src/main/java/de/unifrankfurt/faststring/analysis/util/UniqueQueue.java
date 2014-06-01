package de.unifrankfurt.faststring.analysis.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Queue;

public class UniqueQueue<E> extends LinkedHashSet<E> implements Queue<E> {

	private static final long serialVersionUID = 381094002085132334L;

	public UniqueQueue(Collection<E> coll) {
		super(coll);
	}
	
	@Override
	public boolean offer(E e) {
		return add(e);
	}

	@Override
	public E remove() {
		E e = poll();
		if (e == null) {
			throw new NoSuchElementException();
		}
		return e;
		
	}

	@Override
	public E poll() {
		E e = null;
		if (!isEmpty()) {
			Iterator<E> iter = iterator();
			E removed = iter.next();
			iter.remove();
			return removed;
		}
		return e;
	}

	@Override
	public E element() {
		E e = peek();
		if (e == null) {
			throw new NoSuchElementException();
		}
		return e;
	}

	@Override
	public E peek() {
		E e = null;
		if (!isEmpty()) {
			Iterator<E> iter = iterator();
			e = iter.next();
		}
		return e;
	}
}
