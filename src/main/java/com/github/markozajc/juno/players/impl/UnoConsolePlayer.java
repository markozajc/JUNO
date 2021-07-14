package com.github.markozajc.juno.players.impl;

/**
 * A wrapper for the {@link UnoStreamPlayer} that passes {@link System#in} and
 * {@link System#out} by default (making the game console-based).
 *
 * @author Marko Zajc
 */
public class UnoConsolePlayer extends UnoStreamPlayer {

	/**
	 * Creates a new {@link UnoStreamPlayer}.
	 *
	 * @param name
	 *            hand's name
	 */
	@SuppressWarnings("null")
	public UnoConsolePlayer(String name) {
		super(name, System.in, System.out); // NOSONAR
	}
}
