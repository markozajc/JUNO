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
package org.eu.zajc.juno.decks.impl;

import static java.util.Collections.emptyList;
import static org.eu.zajc.juno.TestUtils.getDummyPlayer;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.decks.UnoDeck;
import org.eu.zajc.juno.decks.impl.UnoStandardDeck;
import org.eu.zajc.juno.players.UnoPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnoStandardDeckTest {

	@Test
	void testSize() {
		UnoDeck deck = UnoStandardDeck.getDeck();
		assertEquals(UnoStandardDeck.getExpectedSize(), deck.getCards().size(),
					 "Invalid UNO deck; the standard UNO deck actual size does not match the expected one.");
	}

	@Test
	@SuppressWarnings("null")
	void testCloning() {
		UnoDeck deck = UnoStandardDeck.getDeck();
		UnoPlayer hand = getDummyPlayer(emptyList());
		deck.getCards().get(0).setPlacer(hand);

		UnoCard card = deck.getCards().get(0);
		assertThrows(IllegalStateException.class, card::getPlacer,
					 "getPlacer() doesn't throw, but the placer was never set.");
	}

}
