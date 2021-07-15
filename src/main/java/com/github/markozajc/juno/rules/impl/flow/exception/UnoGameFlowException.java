package com.github.markozajc.juno.rules.impl.flow.exception;

import com.github.markozajc.juno.rules.types.flow.UnoInitializationConclusion;

/**
 * An exception signaling that something went wrong during UNO game flow calculation.
 *
 * @author Marko Zajc
 *
 * @deprecated Use {@link UnoInitializationConclusion} instead.
 */
@Deprecated
public class UnoGameFlowException extends Exception {

	private final boolean repeat;

	/**
	 * Creates a new {@link UnoGameFlowException}
	 *
	 * @param repeat
	 *            whether to repeat the phase
	 */
	public UnoGameFlowException(boolean repeat) {
		this.repeat = repeat;
	}

	/**
	 * @return whether to repeat the phase
	 */
	public boolean shouldRepeat() {
		return this.repeat;
	}

}
