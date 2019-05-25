package com.github.markozajc.juno.hands.impl;

import javax.annotation.Nonnull;

public class ConsoleUnoHand extends StreamUnoHand {

	@SuppressWarnings("null")
	public ConsoleUnoHand(@Nonnull String name) {
		super(name, System.in, System.out);
	}
}
