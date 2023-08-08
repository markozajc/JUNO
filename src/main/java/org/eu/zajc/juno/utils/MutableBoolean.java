package org.eu.zajc.juno.utils;

/**
 * A class thats records a boolean state for use in lambdas.
 *
 * @author Marko Zajc
 */
public class MutableBoolean {

	private boolean value;

	/**
	 * Sets the value
	 *
	 * @param value
	 *            the new value
	 */
	public void set(boolean value) {
		this.value = value;
	}

	/**
	 * @return the current value
	 */
	public boolean get() {
		return this.value;
	}

}
