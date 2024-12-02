// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java
 * Copyright (C) 2019-2024 Marko Zajc, (Olfi01) Florian Meyer
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
import static java.util.Collections.emptyList;
import static org.eu.zajc.juno.TestUtils.*;
import static org.eu.zajc.juno.cards.UnoCardColor.*;
import static org.eu.zajc.juno.utils.UnoRuleUtils.*;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.*;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.impl.placement.DrawPlacementRules.DrawFourHitchPlacementRule;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules;
import org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UnoRuleUtilsTest {

	@SuppressWarnings("null")
	@Test
	void testGetPlaceableCards() {
		UnoRulePack rules = UnoOfficialRules.getPack();

		UnoCard[] cards = new UnoCard[] {
			/* 0 */ new UnoNumericCard(BLUE, 0),
			/* 1 */ new UnoNumericCard(RED, 0),
			/* 2 */ new UnoNumericCard(YELLOW, 1),
			/* 3 */ new UnoNumericCard(BLUE, 1),
			/* 4 */ new UnoWildCard(),
			/* 5 */ new UnoDrawCard(),
			/* 6 */ new UnoDrawCard(BLUE),
			/* 7 */ new UnoDrawCard(RED),
			/* 8 */ new UnoSkipCard(YELLOW),
			/* 9 */ new UnoReverseCard(GREEN),
			/* 10 */ new UnoReverseCard(BLUE) };

		UnoPlayer player = getDummyPlayer(asList(cards[0]));

		// Card: Blue 0 <0>
		/*
		 * Expected: Blue 0, Red 0, Blue 1, Wild, Blue draw two, Blue reverse
		 */
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[0], asList(cards), rules, player.getHand()),
									   asList(cards[0], cards[1], cards[3], cards[4], cards[6], cards[10])));

		// Card: Red 0 <1>
		/*
		 * Expected: Blue 0, Red 0, Wild, Draw four, Red draw two
		 */
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[1], asList(cards), rules, player.getHand()),
									   asList(cards[0], cards[1], cards[4], cards[5], cards[7])));

		// Card: Yellow 1 <2>
		/*
		 * Expected: Yellow 1, Blue 1, Wild, Draw four, Yellow skip
		 */
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[2], asList(cards), rules, player.getHand()),
									   asList(cards[2], cards[3], cards[4], cards[5], cards[8])));

		// Card: Blue 1 <3>
		/*
		 * Expected: Blue 0, Yellow 1, Blue 1, Wild, Blue draw two, Blue reverse
		 */
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[3], asList(cards), rules, player.getHand()),
									   asList(cards[0], cards[2], cards[3], cards[4], cards[6], cards[10])));

		// Card: Wild <4>
		/*
		 * Expected: Wild, Draw four
		 */
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[4], asList(cards), rules, player.getHand()),
									   asList(cards[4], cards[5])));

		// Card: Wild <4>, masked as Red
		/*
		 * Expected: Blue 0, Blue 1, Wild, Draw four, Blue draw two, Blue reverse
		 */
		cards[4].setColorMask(BLUE);
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[4], asList(cards), rules, player.getHand()),
									   asList(cards[0], cards[3], cards[4], cards[5], cards[6], cards[10])));
		cards[4].reset();

		// Card: Draw four (open) <5>
		/*
		 * Expected: (none)
		 */
		cards[5].markOpen();
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[5], asList(cards), rules, player.getHand()),
									   emptyList()));
		cards[5].reset();

		// Card: Blue draw two (open) <6>
		/*
		 * Expected: (none)
		 */
		cards[6].markOpen();
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[6], asList(cards), rules, player.getHand()),
									   emptyList()));
		cards[6].reset();

		// Card: Red draw two (open) <7>
		/*
		 * Expected: (none)
		 */
		cards[7].markOpen();
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[7], asList(cards), rules, player.getHand()),
									   emptyList()));
		cards[7].reset();

		// Card: Yellow skip <8>
		/*
		 * Expected: Yellow 1, Wild, Wild draw four, Yellow skip
		 */
		cards[8].markOpen();
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[8], asList(cards), rules, player.getHand()),
									   asList(cards[2], cards[4], cards[5], cards[8])));
		cards[8].reset();

		// Card: Green reverse <9>
		/*
		 * Expected: Wild, Draw four, Green reverse, Blue reverse
		 */
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[9], asList(cards), rules, player.getHand()),
									   asList(cards[4], cards[5], cards[9], cards[10])));

		// Card: Blue reverse <10>
		/*
		 * Expected: Blue 0, Blue 1, Wild, Blue draw two, Green reverse, Blue reverse
		 */
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[10], asList(cards), rules, player.getHand()),
									   asList(cards[0], cards[3], cards[4], cards[6], cards[9], cards[10])));

		// Card: Red draw two (closed) <7>
		/*
		 * Expected: Red 0, Wild, Draw four, Blue draw two, Red draw two
		 */
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[7], asList(cards), rules, player.getHand()),
									   asList(cards[1], cards[4], cards[5], cards[6], cards[7])));

		// Switch to progressive rules
		rules = UnoOfficialRules.getPack(UnoHouseRule.PROGRESSIVE);

		// Card: Draw four (open) <5>
		/*
		 * Expected: Draw four
		 */
		cards[5].markOpen();
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[5], asList(cards), rules, player.getHand()),
									   asList(cards[5])));
		cards[5].reset();

		// Card: Blue draw two (open) <6>
		/*
		 * Expected: Blue draw two, Red draw two
		 */
		cards[6].markOpen();
		assertTrue(listEqualsUnordered(getPlaceableCards(cards[6], asList(cards), rules, player.getHand()),
									   asList(cards[6], cards[7])));
		cards[6].reset();
	}

	@SuppressWarnings("null")
	@Test
	void testGetProhibitingRules() {
		UnoRulePack rules = UnoOfficialRules.getPack();

		UnoCard[] cards = new UnoCard[] {
			/* 0 */ new UnoNumericCard(BLUE, 0),
			/* 1 */ new UnoNumericCard(RED, 0),
			/* 2 */ new UnoNumericCard(YELLOW, 1),
			/* 3 */ new UnoNumericCard(BLUE, 1),
			/* 4 */ new UnoWildCard(),
			/* 5 */ new UnoDrawCard(),
			/* 6 */ new UnoDrawCard(BLUE),
			/* 7 */ new UnoDrawCard(RED),
			/* 8 */ new UnoSkipCard(YELLOW),
			/* 9 */ new UnoReverseCard(GREEN),
			/* 10 */ new UnoReverseCard(BLUE) };

		UnoPlayer player = getDummyPlayer(asList(cards[0]));

		assertTrue(listEqualsUnordered(getProhibitingRules(cards[0], cards[5], rules, player.getHand()),
									   filterRuleKind(rules.getRules(), DrawFourHitchPlacementRule.class)));
	}

}
