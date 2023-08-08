package org.eu.zajc.juno.players.impl;

import static java.lang.System.*;

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
		super(name, in, out); // NOSONAR
	}
}
