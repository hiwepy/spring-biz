package org.springframework.biz.utils;

import java.util.Iterator;
import java.util.Enumeration;

/**
 * An Iterator wrapper for an Enumeration.
 */
public class EnumerationIterator<T> implements Iterator<T> {
	/**
	 * The enumeration to iterate.
	 */
	private Enumeration<T> enumeration = null;

	/**
	 * Creates a new iteratorwrapper instance for the specified Enumeration.
	 *
	 * @param enumeration
	 *            The Enumeration to wrap.
	 */
	public EnumerationIterator(Enumeration<T> enumeration) {
		this.enumeration = enumeration;
	}

	/**
	 * Move to next element in the array.
	 *
	 * @return The next object in the array.
	 */
	public T next() {
		return enumeration.nextElement();
	}

	/**
	 * Check to see if there is another element in the array.
	 *
	 * @return Whether there is another element.
	 */
	public boolean hasNext() {
		return enumeration.hasMoreElements();
	}

	/**
	 * Unimplemented. No analogy in Enumeration
	 */
	public void remove() {
		// not implemented
	}

}
