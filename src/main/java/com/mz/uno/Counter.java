package com.mz.uno;

/**
 * An object used to represent a simple counter
 * 
 * @author Marko Zajc
 */
public class Counter {

	private int count;

	/**
	 * Creates a new simple counter with initial count of 0
	 */
	public Counter() {
		this(0);
	}

	/**
	 * Creates a new simple counter with initial count of X
	 * 
	 * @param initialCount
	 *            initial count
	 */
	public Counter(int initialCount) {
		this.count = initialCount;
	}

	/**
	 * Counts up
	 */
	public void count() {
		this.count++;
	}

	/**
	 * Resets the counter to 0
	 */
	public void reset() {
		this.count = 0;
	}

	/**
	 * Gets the count
	 * 
	 * @return count
	 */
	public int getCount() {
		return this.count;
	}

}
