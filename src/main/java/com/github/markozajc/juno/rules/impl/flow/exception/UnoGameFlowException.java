package com.github.markozajc.juno.rules.impl.flow.exception;

public class UnoGameFlowException extends Exception {

	private final boolean repeat;

	public UnoGameFlowException(boolean repeat) {
		this.repeat = repeat;
	}

	public boolean shouldRepeat() {
		return this.repeat;
	}

}
