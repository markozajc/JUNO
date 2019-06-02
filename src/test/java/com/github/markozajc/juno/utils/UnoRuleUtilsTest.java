package com.github.markozajc.juno.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.TestUtils;
import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.cards.impl.UnoActionCard;
import com.github.markozajc.juno.cards.impl.UnoActionCard.UnoAction;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.cards.impl.UnoNumericCard;
import com.github.markozajc.juno.cards.impl.UnoWildCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;

class UnoRuleUtilsTest {

	@SuppressWarnings("null")
	@Test
	void testAnalyzePossibleCards() {
		UnoRulePack pack = UnoOfficialRules.getPack();

		UnoCard[] cards = new UnoCard[] {
				/* 0 */ new UnoNumericCard(UnoCardColor.BLUE, 0),
				/* 1 */ new UnoNumericCard(UnoCardColor.RED, 0),
				/* 2 */ new UnoNumericCard(UnoCardColor.YELLOW, 1),
				/* 3 */ new UnoNumericCard(UnoCardColor.BLUE, 1),
				/* 4 */ new UnoWildCard(),
				/* 5 */ new UnoDrawCard(),
				/* 6 */ new UnoDrawCard(UnoCardColor.BLUE),
				/* 7 */ new UnoDrawCard(UnoCardColor.RED),
				/* 8 */ new UnoActionCard(UnoCardColor.YELLOW, UnoAction.SKIP),
				/* 9 */ new UnoActionCard(UnoCardColor.GREEN, UnoAction.REVERSE),
				/* 10 */ new UnoActionCard(UnoCardColor.BLUE, UnoAction.REVERSE)

		};
		// Creates a list of cards to be tested
		/*
		 * Cards: Blue 0, Red 0, Yellow 1, Blue 1, Wild, Draw four, Blue draw two, Red draw
		 * two, Yellow skip, Green reverse, Blue reverse
		 */

		UnoHand hand = TestUtils.getDummyHand(Arrays.asList(cards[0]));

		assertTrue(TestUtils.listEqualsUnordered(
			UnoRuleUtils.combinedPlacementAnalysis(cards[0], Arrays.asList(cards), pack, hand),
			Arrays.asList(cards[0], cards[1], cards[3], cards[4], cards[6], cards[10])));
		// Pack: official
		// Card: Blue 0 <0>
		/*
		 * Expected: Blue 0, Red 0, Blue 1, Wild, Blue draw two, Blue reverse
		 */

		assertTrue(TestUtils.listEqualsUnordered(
			UnoRuleUtils.combinedPlacementAnalysis(cards[1], Arrays.asList(cards), pack, hand),
			Arrays.asList(cards[0], cards[1], cards[4], cards[5], cards[7])));
		// Pack: official
		// Card: Red 0 <1>
		/*
		 * Expected: Blue 0, Red 0, Wild, Draw four, Red draw two
		 */

		assertTrue(TestUtils.listEqualsUnordered(
			UnoRuleUtils.combinedPlacementAnalysis(cards[2], Arrays.asList(cards), pack, hand),
			Arrays.asList(cards[2], cards[3], cards[4], cards[5], cards[8])));
		// Pack: official
		// Card: Yellow 1 <2>
		/*
		 * Expected: Yellow 1, Blue 1, Wild, Draw four, Yellow skip
		 */

		assertTrue(TestUtils.listEqualsUnordered(
			UnoRuleUtils.combinedPlacementAnalysis(cards[3], Arrays.asList(cards), pack, hand),
			Arrays.asList(cards[0], cards[2], cards[3], cards[4], cards[6], cards[10])));
		// Pack: official
		// Card: Blue 1 <3>
		/*
		 * Expected: Blue 0, Yellow 1, Blue 1, Wild, Blue draw two, Blue reverse
		 */

		assertTrue(TestUtils.listEqualsUnordered(
			UnoRuleUtils.combinedPlacementAnalysis(cards[4], Arrays.asList(cards), pack, hand),
			Arrays.asList(cards[4], cards[5])));
		// Pack: official
		// Card: Wild <4>
		/*
		 * Expected: Wild, Draw four
		 */

		cards[4].setColorMask(UnoCardColor.BLUE);
		assertTrue(TestUtils.listEqualsUnordered(
			UnoRuleUtils.combinedPlacementAnalysis(cards[4], Arrays.asList(cards), pack, hand),
			Arrays.asList(cards[0], cards[3], cards[4], cards[5], cards[6], cards[10])));
		// Pack: official
		// Card: Wild <4>, masked as Red
		/*
		 * Expected: Blue 0, Blue 1, Wild, Draw four, Blue draw two, Blue reverse
		 */
		cards[4].reset();

		assertTrue(TestUtils.listEqualsUnordered(
			UnoRuleUtils.combinedPlacementAnalysis(cards[5], Arrays.asList(cards), pack, hand),
			Collections.emptyList()));
		// Pack: official
		// Card: Draw four (unplayed) <5>
		/*
		 * Expected: (none)
		 */

		assertTrue(TestUtils.listEqualsUnordered(
			UnoRuleUtils.combinedPlacementAnalysis(cards[6], Arrays.asList(cards), pack, hand),
			Collections.emptyList()));
		// Pack: official
		// Card: Blue draw two (unplayed) <6>
		/*
		 * Expected: (none)
		 */

		assertTrue(TestUtils.listEqualsUnordered(
			UnoRuleUtils.combinedPlacementAnalysis(cards[7], Arrays.asList(cards), pack, hand),
			Collections.emptyList()));
		// Pack: official
		// Card: Red draw two (unplayed) <7>
		/*
		 * Expected: (none)
		 */

		assertTrue(TestUtils.listEqualsUnordered(
			UnoRuleUtils.combinedPlacementAnalysis(cards[8], Arrays.asList(cards), pack, hand),
			Arrays.asList(cards[2], cards[4], cards[5], cards[8])));
		// Pack: official
		// Card: Yellow skip <8>
		/*
		 * Expected: Yellow 1, Wild, Wild draw four, Yellow skip
		 */

		assertTrue(TestUtils.listEqualsUnordered(
			UnoRuleUtils.combinedPlacementAnalysis(cards[9], Arrays.asList(cards), pack, hand),
			Arrays.asList(cards[4], cards[5], cards[9], cards[10])));
		// Pack: official
		// Card: Green reverse <9>
		/*
		 * Expected: Wild, Draw four, Green reverse, Blue reverse
		 */

		assertTrue(TestUtils.listEqualsUnordered(
			UnoRuleUtils.combinedPlacementAnalysis(cards[10], Arrays.asList(cards), pack, hand),
			Arrays.asList(cards[0], cards[3], cards[4], cards[6], cards[9], cards[10])));
		// Pack: official
		// Card: Blue reverse <10>
		/*
		 * Expected: Blue 0, Blue 1, Wild, Blue draw two, Green reverse, Blue reverse
		 */

		// TODO
		// Pack: official, progressive
		// Card: Draw four (unplayed) <5>
		/*
		 * Expected: Draw four
		 */

		// TODO
		// Pack: official, progressive
		// Card: Blue draw two (unplayed) <6>
		/*
		 * Expected: Blue draw two, Red draw two
		 */

		// TODO
		// Pack: official
		// Card: Red draw two (unplayed) <7>
		/*
		 * Expected: Blue draw two, Red draw two
		 */

	}

}
