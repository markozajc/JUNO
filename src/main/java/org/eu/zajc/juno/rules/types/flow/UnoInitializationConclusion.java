// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java
 * Copyright (C) 2019-2024 Marko Zajc
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
package org.eu.zajc.juno.rules.types.flow;

import org.eu.zajc.juno.hands.UnoHand;

/**
 * An extension of {@link UnoPhaseConclusion} that also lets you choose whether the
 * {@link UnoHand} loses a turn or not.
 *
 * @author Marko Zajc
 */
public class UnoInitializationConclusion extends UnoPhaseConclusion {

	/**
	 * Same as using the no-arguments constructor, but as a constant.
	 */
	public static final UnoInitializationConclusion NOTHING = new UnoInitializationConclusion();

	private final boolean shouldLoseATurn;

	/**
	 * Creates a new {@link UnoInitializationConclusion} that doesn't change anything.
	 */
	public UnoInitializationConclusion() {
		this(false, false);
	}

	/**
	 * Creates a new {@link UnoInitializationConclusion}.
	 *
	 * @param shouldRepeat
	 *            whether the entire phase should be repeated
	 * @param shouldLoseATurn
	 *            whether the {@link UnoHand} loses the turn
	 */
	public UnoInitializationConclusion(boolean shouldRepeat, boolean shouldLoseATurn) {
		super(shouldRepeat, false);
		this.shouldLoseATurn = shouldLoseATurn;
	}

	/**
	 * @return whether the {@link UnoHand} loses the turn
	 */
	public boolean shouldLoseATurn() {
		return this.shouldLoseATurn;
	}

}
