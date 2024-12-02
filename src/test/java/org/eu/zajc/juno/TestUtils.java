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
package org.eu.zajc.juno;

import static java.lang.System.*;
import static java.util.stream.Collectors.joining;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.eu.zajc.juno.cards.*;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.players.UnoPlayer;

/**
 * This is not actually a test class. Instead it provides shared utility methods to
 * the tests themselves. Acquired with {@link #createCheckState()}.
 *
 * @author Marko Zajc
 */
public class TestUtils {

	/**
	 * A class storing a boolean state
	 *
	 * @author Marko Zajc
	 */
	public static class CheckState {

		private boolean state = false;

		CheckState() {}

		/**
		 * @return the state
		 */
		public boolean getState() {
			return this.state;
		}

		/**
		 * Sets the state
		 *
		 * @param state
		 *            the new state
		 */
		public void setState(boolean state) {
			this.state = state;
		}

	}

	private static class DummyUnoPlayer extends UnoPlayer {

		public DummyUnoPlayer(@Nonnull Collection<UnoCard> cards) {
			super("DummyUnoPlayer-" + currentTimeMillis());
			this.getHand().getCards().addAll(cards);
		}

		@Override
		public UnoCard playCard(UnoGame game) {
			throw new UnsupportedOperationException("DummyUnoHand can not play cards.");
		}

		@Override
		public UnoCardColor chooseColor(UnoGame game) {
			throw new UnsupportedOperationException("DummyUnoHand can not choose colors.");
		}

		@Override
		public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard) {
			throw new UnsupportedOperationException("DummyUnoHand can not decide whether it should play drawn cards or not.");
		}

	}

	/**
	 * Checks the equality of two {@link Collection}s in an unordered manner.
	 *
	 * @param <T>
	 *            collection item type
	 * @param collection1
	 *            collection number one
	 * @param collection2
	 *            collection number two
	 *
	 * @return whether the collection contain equal items or not
	 */
	public static <T> boolean listEqualsUnordered(Collection<? extends T> collection1,
												  Collection<? extends T> collection2) {
		out.println("[= COMPARING COLLECTIONS /unordered =]");
		out.println("Collection 1: " + collection1.stream().map(Object::toString).collect(joining(",")));
		out.println("Collection 2: " + collection2.stream().map(Object::toString).collect(joining(",")));
		return collection1.size() == collection2.size() && collection1.containsAll(collection2);
	}

	/**
	 * Returns a dummy {@link UnoPlayer} containing a preferred {@link Collection} of
	 * {@link UnoCard}s. Calling either {@link UnoPlayer#playCard(UnoGame)},
	 * {@link UnoPlayer#chooseColor(UnoGame)} or
	 * {@link UnoPlayer#shouldPlayDrawnCard(UnoGame, UnoCard)} will throw an
	 * {@link UnsupportedOperationException}.
	 *
	 * @param cards
	 *            {@link Collection} of {@link UnoCard}s the {@link UnoHand} should
	 *            contain
	 *
	 * @return the created {@link UnoPlayer}
	 */
	@Nonnull
	public static DummyUnoPlayer getDummyPlayer(@Nonnull Collection<UnoCard> cards) {
		return new DummyUnoPlayer(cards);
	}

	/**
	 * Creates a new {@link CheckState}.
	 *
	 * @return the new {@link CheckState}
	 */
	public static CheckState createCheckState() {
		return new CheckState();
	}

}
