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
package org.eu.zajc.juno.utils;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.eu.zajc.juno.TestUtils.createCheckState;
import static org.eu.zajc.juno.cards.UnoCardColor.*;
import static org.eu.zajc.juno.utils.UnoGameUtils.drawCards;

import java.util.*;

import org.eu.zajc.juno.TestUtils;
import org.eu.zajc.juno.TestUtils.CheckState;
import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.UnoNumericCard;
import org.eu.zajc.juno.decks.UnoDeck;
import org.eu.zajc.juno.decks.impl.UnoStandardDeck;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.piles.impl.*;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnoGameUtilsTest {

	private static class FakeUnoGame extends UnoGame {

		private final UnoDrawPile draw;
		private final UnoDiscardPile discard;
		private final CheckState checker;

		@SuppressWarnings("null")
		public FakeUnoGame(UnoDrawPile draw, UnoDiscardPile discard, CheckState checker) {
			super(UnoStandardDeck.getDeck(), 0, UnoOfficialRules.getPack(),
					TestUtils.getDummyPlayer(Collections.emptyList()), TestUtils.getDummyPlayer(Collections.emptyList()));

			this.checker = checker;
			this.draw = draw;
			this.discard = discard;
		}

		@Override
		protected void turn(UnoPlayer player) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void onEvent(String format, Object... arguments) {
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("null")
		@Override
		public UnoDrawPile getDraw() {
			return this.draw;
		}

		@SuppressWarnings("null")
		@Override
		public UnoDiscardPile getDiscard() {
			return this.discard;
		}

		@Override
		public void discardIntoDraw() {
			super.discardIntoDraw();

			this.checker.setState(true);
		}

	}

	@SuppressWarnings("null")
	@Test
	void testDrawCards() {
		UnoDeck deck = new UnoDeck(asList(new UnoNumericCard(RED, 0)));
		/*
		 * Cards: Red 0
		 */

		List<UnoCard> toDiscard = asList(new UnoNumericCard(GREEN, 1), new UnoNumericCard(BLUE, 2));
		/*
		 * Cards: Green 1, Blue 2
		 */

		CheckState checker = createCheckState();
		// Creates a CheckState used to check whether #discardIntoDraw has been called or not

		UnoGame game = new FakeUnoGame(new UnoDrawPile(deck), new UnoDiscardPile(), checker);
		// Creates a new FakeUnoGame

		game.getDiscard().addAll(toDiscard);
		// Adds two cards to the discard (one is top and one will be merged into the draw
		// pile)

		assertEquals(deck.getCards().get(0).getColor(), drawCards(game, 1).get(0).getColor());
		// Checks whether the drawn card is the right one (by color)

		assertFalse(checker.getState());
		// #discardIntoDraw should not have been called at this point

		assertTrue(toDiscard.stream()
			.map(UnoCard::getColor)
			.collect(toList())
			.contains(drawCards(game, 1).get(0).getColor()));
		// Checks whether the drawn card is the right one (by color)

		assertTrue(checker.getState());
		// #discardIntoDraw should have been called at this point
	}

}
