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
package org.eu.zajc.juno.game;

import javax.annotation.Nullable;

import org.eu.zajc.juno.piles.impl.UnoDrawPile;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.UnoRule;
import org.eu.zajc.juno.rules.pack.impl.house.UnoFoeWinsOnEndRequestPack;

/**
 * Contains the winner of a game, as well as the reason a game has ended. The winner
 * can be altered by a custom {@link UnoRule} (such as
 * {@link UnoFoeWinsOnEndRequestPack}), in which case {@link #isModified()} returns
 * {@code true}.
 *
 * @author Marko Zajc
 *
 */
public class UnoWinner {

	@Nullable
	private UnoPlayer winner;
	private boolean modified;
	@Nullable
	private final UnoEndReason endReason;

	UnoWinner(@Nullable UnoPlayer winner, UnoEndReason endReason) {
		this.winner = winner;
		this.endReason = endReason;
	}

	/**
	 * @return the winner {@link UnoPlayer} or {@code null} if the game was a draw
	 */
	@Nullable
	public UnoPlayer getWinner() {
		return this.winner;
	}

	/**
	 * @return whether or not the winner was altered by a custom {@link UnoRule}
	 */
	public boolean isModified() {
		return this.modified;
	}

	/**
	 * @return whether or not the winner was altered by a {@link UnoRule}
	 */
	public UnoEndReason getEndReason() {
		return this.endReason;
	}

	void setNewWinner(@Nullable UnoPlayer winner) {
		this.winner = winner;
		this.modified = true;
	}

	/**
	 * The reason that a {@link UnoGame} has ended.
	 */
	public enum UnoEndReason {

		/**
		 * The end was requested by calling {@link UnoGame#endGame()}. By default this
		 * produces a draw ({@code null} {@link UnoWinner#getWinner()}).
		 */
		REQUESTED,
		/**
		 * The game ran out of cards in its {@link UnoDrawPile}. By default the hand with
		 * less cards wins in this scenario.
		 */
		FALLBACK,

		/**
		 * A {@link UnoPlayer} has placed all of its cards. This marks the player victorious
		 * in UNO.
		 */
		VICTORY,

		/**
		 * The game ended for an unknown reason. Should never occur.
		 */
		UNKNOWN
	}

}
