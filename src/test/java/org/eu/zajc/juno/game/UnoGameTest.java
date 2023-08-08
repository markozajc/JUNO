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
package org.eu.zajc.juno.game;

import static java.util.Collections.emptyList;
import static org.eu.zajc.juno.TestUtils.getDummyPlayer;
import static org.eu.zajc.juno.decks.impl.UnoStandardDeck.getDeck;
import static org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule.PROGRESSIVE;

import javax.annotation.*;

import org.eu.zajc.juno.decks.UnoDeck;
import org.eu.zajc.juno.game.*;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnoGameTest {

	private static final class UnoTestGame extends UnoControlledGame {

		public UnoTestGame(@Nonnull UnoDeck unoDeck, @Nonnegative int cardAmount, @Nonnull UnoRulePack rules,
						   @Nonnull UnoPlayer... players) {
			super(unoDeck, cardAmount, rules, players);
		}

		@Override
		public void onEvent(String format, Object... arguments) {}
	}

	@Test
	@SuppressWarnings("null")
	void testGetNextPlayer() {
		var first = getDummyPlayer(emptyList());
		var second = getDummyPlayer(emptyList());
		var third = getDummyPlayer(emptyList());
		var game = createGame(first, second, third);

		assertEquals(second, game.getNextPlayer(first));
		assertEquals(third, game.getNextPlayer(second));
		assertEquals(first, game.getNextPlayer(third));

		game.reverseDirection();
		assertEquals(second, game.getNextPlayer(third));
		assertEquals(third, game.getNextPlayer(first));
		assertEquals(first, game.getNextPlayer(second));
	}

	@Test
	@SuppressWarnings("null")
	void testGetPreviousPlayer() {
		var first = getDummyPlayer(emptyList());
		var second = getDummyPlayer(emptyList());
		var third = getDummyPlayer(emptyList());
		var game = createGame(first, second, third);

		assertEquals(second, game.getPreviousPlayer(third));
		assertEquals(third, game.getPreviousPlayer(first));
		assertEquals(first, game.getPreviousPlayer(second));

		game.reverseDirection();
		assertEquals(second, game.getPreviousPlayer(first));
		assertEquals(third, game.getPreviousPlayer(second));
		assertEquals(first, game.getPreviousPlayer(third));
	}

	private static UnoGame createGame(@Nonnull UnoPlayer... players) {
		return new UnoTestGame(getDeck(), 7, UnoOfficialRules.getPack(PROGRESSIVE), players);
	}

}
