package com.github.markozajc.juno.hands.impl;

import javax.annotation.Nonnull;

/**
 * A wrapper for the {@link StreamUnoHand} that passes {@link System#in} and
 * {@link System#out} by default (making the game console-based).
 *
 * @author Marko Zajc
 */
public class ConsoleUnoHand extends StreamUnoHand {

	/**
	 * Creates a new {@link StreamUnoHand}.
	 *
	 * @param name hand's name
	 */
	@SuppressWarnings("null")
	public ConsoleUnoHand(@Nonnull String name) {
		super(name, System.in, System.out); // NOSONAR
	}
}
