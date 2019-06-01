package com.github.markozajc.juno.rules.impl.flow.exception;

import com.github.markozajc.juno.rules.types.flow.UnoTurnInitializationConclusion;

/**
 * An exception signaling that something went wrong during UNO game flow calculation.
 *
 * @author Marko Zajc
 * @deprecated Use {@link UnoTurnInitializationConclusion} instead.
 */
@Deprecated
public class UnoGameFlowException extends Exception {

	private final boolean repeat;

	public UnoGameFlowException(boolean repeat) {
		this.repeat = repeat;
	}

	public boolean shouldRepeat() {
		return this.repeat;
	}

}
