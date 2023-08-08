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
package org.eu.zajc.juno.hands;

import static org.eu.zajc.juno.cards.UnoCardColor.*;

import org.eu.zajc.juno.cards.impl.UnoNumericCard;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.piles.impl.UnoDiscardPile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnoHandTest {

	@SuppressWarnings("null")
	@Test
	void testAddToDiscard() {
		UnoHand hand = new UnoHand();
		UnoNumericCard card = new UnoNumericCard(RED, 0);
		hand.getCards().add(card);
		// Adds a card to the hand

		UnoDiscardPile discard = new UnoDiscardPile();
		// Creates a new discard pile

		assertTrue(hand.addToDiscard(discard, hand.getCards().get(0)));
		// Adds a card from the hand

		assertTrue(hand.getCards().isEmpty());
		assertEquals(card, discard.getTop());
		// Tests the situation

		assertFalse(hand.addToDiscard(discard, card.cloneCard()));
		// Tests the nonexistent card block
	}

	@Test
	void testClear() {
		UnoHand hand = new UnoHand();

		hand.getCards().add(new UnoNumericCard(RED, 0));
		hand.getCards().add(new UnoNumericCard(BLUE, 1));
		hand.getCards().add(new UnoNumericCard(GREEN, 2));
		hand.getCards().add(new UnoNumericCard(YELLOW, 3));
		// Adds a some cards to the hand

		assertEquals(4, hand.getSize());
		// Tests the hand size

		hand.clear();
		// Clears the hand

		assertEquals(0, hand.getSize());
		// Tests the hand size
	}

}
