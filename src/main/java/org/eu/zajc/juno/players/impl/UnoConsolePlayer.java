// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java 
 * Copyright (C) 2019-2023 Marko Zajc
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <https://www.gnu.org/licenses/>.
 */
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
