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
package org.eu.zajc.juno.rules.types.flow;

import javax.annotation.Nonnull;

import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.types.UnoGameFlowRule;

/**
 * A return value class that lets {@link UnoGameFlowRule}s indicate whether or not to
 * change the winner of a game.
 *
 * @author Marko Zajc
 */
public class UnoFinishConclusion {

	/**
	 * Same as using the no-arguments constructor, but as a constant.
	 */
	public static final UnoFinishConclusion NOTHING = new UnoFinishConclusion();

	private final boolean objectWinner;
	private final UnoPlayer newWinner;

	/**
	 * Creates a new {@link UnoFinishConclusion} that doesn't change anything.
	 */
	public UnoFinishConclusion() {
		this.objectWinner = false;
		this.newWinner = null;
	}

	/**
	 * Creates a new {@link UnoFinishConclusion}.
	 *
	 * @param newWinner
	 *            the new winning {@link UnoPlayer} or {@code null} for a draw
	 */
	public UnoFinishConclusion(@Nonnull UnoPlayer newWinner) {
		this.objectWinner = true;
		this.newWinner = newWinner;

	}

	/**
	 * @return whether to change the winner
	 */
	public boolean doesObjectWinner() {
		return this.objectWinner;
	}

	/**
	 * @return what the new winner should be (irrelevant if {@link #doesObjectWinner()}
	 *         is false)
	 */
	public UnoPlayer getNewWinner() {
		return this.newWinner;
	}

}
