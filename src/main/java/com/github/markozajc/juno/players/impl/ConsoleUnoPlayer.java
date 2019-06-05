package com.github.markozajc.juno.players.impl;

/**
 * A wrapper for the {@link StreamUnoPlayer} that passes {@link System#in} and
 * {@link System#out} by default (making the game console-based).
 *
 * @author Marko Zajc
 */
public class ConsoleUnoPlayer extends StreamUnoPlayer {

	/**
	 * Creates a new {@link StreamUnoPlayer}.
	 *
	 * @param name hand's name
	 */
	@SuppressWarnings("null")
	public ConsoleUnoPlayer(String name) {
		super(name, System.in, System.out); // NOSONAR
	}
}
