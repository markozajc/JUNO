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
package org.eu.zajc.juno.cards;

import static java.util.Collections.emptyList;
import static org.eu.zajc.juno.TestUtils.getDummyPlayer;
import static org.eu.zajc.juno.cards.UnoCardColor.*;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.*;
import org.eu.zajc.juno.players.UnoPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnoCardTest {

	@Test
	void testColor() {
		UnoCard wild = new UnoWildCard();
		// Creates a new wild-colored card

		wild.setColorMask(RED);
		// Set the color mask to red

		assertEquals(RED, wild.getColor());
		// Test the color mask

		assertEquals(WILD, wild.getOriginalColor());
		// Test the original color

		assertThrows(IllegalStateException.class, () -> wild.setColorMask(GREEN));
		// Test the multicall prevention

		///////////////////////////////////////////////////////////////////////////////////

		UnoCard nonWild = new UnoNumericCard(RED, 0);
		// Creates a new red-colored card

		assertThrows(IllegalStateException.class, () -> nonWild.setColorMask(GREEN));
		// Test the non-wild color mask block

		assertEquals(RED, nonWild.getColor());
		// Test if the color is still red
	}

	@Test
	@SuppressWarnings("null")
	void testPlacer() {
		UnoCard card = new UnoNumericCard(RED, 0);
		// Creates a new numeric card

		assertThrows(IllegalStateException.class, () -> card.getPlacer());
		// Test the no-placer getCall exception

		UnoPlayer placer = getDummyPlayer(emptyList());
		// Create a new dummy player

		card.setPlacer(placer);
		// Sets the card's placer

		assertEquals(placer, card.getPlacer());
		// Tests if the card's placer is right

		assertThrows(IllegalStateException.class, () -> card.setPlacer(placer));
		// Test the multicall prevention
	}

	@Test
	@SuppressWarnings("null")
	void testReset() {
		UnoCard card = new UnoWildCard();
		// Creates a new numeric card

		card.setPlacer(getDummyPlayer(emptyList()));
		card.setColorMask(GREEN);
		card.markOpen();
		// Sets the card's state

		card.reset();
		// Reset the card

		assertThrows(IllegalStateException.class, () -> card.getPlacer());
		assertEquals(WILD, card.getColor());
		assertFalse(card.isOpen());
		// Test the state

	}

}
