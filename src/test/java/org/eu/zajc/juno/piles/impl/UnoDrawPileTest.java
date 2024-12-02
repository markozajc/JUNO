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
package org.eu.zajc.juno.piles.impl;

import static java.util.Arrays.asList;
import static org.eu.zajc.juno.cards.UnoCardColor.*;

import java.util.Random;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.*;
import org.eu.zajc.juno.decks.UnoDeck;
import org.eu.zajc.juno.decks.impl.UnoStandardDeck;
import org.eu.zajc.juno.piles.impl.UnoDrawPile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnoDrawPileTest {

	@Test
	void testCreation() {
		UnoDeck deck = UnoStandardDeck.getDeck();
		// Defines the deck to use

		UnoDrawPile pile = new UnoDrawPile(deck, new Random(0)); // NOSONAR determinism
		// Creates a new draw pile from the standard deck

		assertEquals(UnoStandardDeck.getExpectedSize(), pile.getSize(),
					 "Size of deck and pile differs, most likely due to a deck copying failure.");
		// Checks whether the deck and the pile are of the same size
	}

	@Test
	void testShuffle() {
		@SuppressWarnings("null")
		UnoDeck deck =
			new UnoDeck(asList(new UnoNumericCard(RED, 0), new UnoNumericCard(GREEN, 1), new UnoNumericCard(BLUE, 2),
							   new UnoNumericCard(YELLOW, 3), new UnoNumericCard(RED, 4), new UnoNumericCard(GREEN, 5),
							   new UnoNumericCard(BLUE, 6), new UnoNumericCard(YELLOW, 7), new UnoNumericCard(RED, 8),
							   new UnoNumericCard(GREEN, 9)));
		// Creates a new dummy deck

		UnoDrawPile pile = new UnoDrawPile(deck, new Random(0)); // NOSONAR determinism
		// Creates a new UnoDrawPile. Cards should be shuffled upon creation.

		assertNotEquals(deck.getCards(), pile.getCards(), "The resulting deck is not shuffled.");
		// Tests the equality of the lists. They shouldn't be equal because one is shuffled
		// and the other is not.
	}

	@Test
	void testDrawInitialCard() {
		@SuppressWarnings("null")
		UnoDeck deck = new UnoDeck(asList(new UnoWildCard(), new UnoNumericCard(RED, 0)));
		// Defines the deck to use

		UnoDrawPile pile = new UnoDrawPile(deck, new Random(0)); // NOSONAR determinism
		// Creates a new draw pile from the standard deck

		UnoCard initial = pile.drawInitalCard();
		// Draws the initial card

		assertTrue(initial instanceof UnoNumericCard, "The initial card is not a UnoNumericCard");
		// Checks the initial card

		assertEquals(1, pile.getSize(), "The size of the pile has not been reduced by one after drawing a card.");
		// Checks whether the card has been actually drawn from the pile
	}

}
