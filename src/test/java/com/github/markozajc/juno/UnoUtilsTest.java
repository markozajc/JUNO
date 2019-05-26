package com.github.markozajc.juno;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.cards.impl.UnoActionCard;
import com.github.markozajc.juno.cards.impl.UnoActionCard.UnoAction;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.cards.impl.UnoNumericCard;
import com.github.markozajc.juno.cards.impl.UnoWildCard;
import com.github.markozajc.juno.utils.UnoUtils;

class UnoUtilsTest {

	private static boolean checkColorAnalysis(Entry<Long, UnoCardColor> entry, long expectedQuantity, UnoCardColor expectedColor) {
		System.out.println("[========= TESTING ANALYSIS =========]");
		System.out.println("<quantity> " + entry.getKey() + " : " + expectedQuantity);
		System.out.println("< color  > " + entry.getValue() + " : " + expectedColor);

		return entry.getKey() == expectedQuantity && entry.getValue().equals(expectedColor);
	}

	@Test
	void testAnalyzePossibleCards() {
		UnoCard[] cards = new UnoCard[] {
				/* 0 */ new UnoNumericCard(0, UnoCardColor.BLUE),
				/* 1 */ new UnoNumericCard(0, UnoCardColor.RED),
				/* 2 */ new UnoNumericCard(1, UnoCardColor.YELLOW),
				/* 3 */ new UnoNumericCard(1, UnoCardColor.BLUE),
				/* 4 */ new UnoWildCard(),
				/* 5 */ new UnoDrawCard(),
				/* 6 */ new UnoDrawCard(UnoCardColor.BLUE),
				/* 7 */ new UnoDrawCard(UnoCardColor.RED),
				/* 8 */ new UnoActionCard(UnoAction.SKIP, UnoCardColor.YELLOW),
				/* 9 */ new UnoActionCard(UnoAction.REVERSE, UnoCardColor.GREEN),
				/* 10 */ new UnoActionCard(UnoAction.REVERSE, UnoCardColor.BLUE)

		};

		/*
		 * Cards: Blue 0, Red 0, Yellow 1, Blue 1, Wild, Draw four, Blue draw two, Red draw
		 * two, Yellow skip, Green reverse, Blue reverse
		 */

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.analyzePossibleCards(cards[0], Arrays.asList(cards)),
			Arrays.asList(cards[0], cards[1], cards[3], cards[4], cards[5], cards[6], cards[10])));
		// Card: Blue 0 <0>
		/*
		 * Expected: Blue 0, Red 0, Blue 1, Wild, Draw four, Blue draw two, Blue reverse
		 */

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.analyzePossibleCards(cards[1], Arrays.asList(cards)),
			Arrays.asList(cards[0], cards[1], cards[4], cards[5], cards[7])));
		// Card: Red 0 <1>
		/*
		 * Expected: Blue 0, Red 0, Wild, Draw four, Red draw two
		 */

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.analyzePossibleCards(cards[2], Arrays.asList(cards)),
			Arrays.asList(cards[2], cards[3], cards[4], cards[5], cards[8])));
		// Card: Yellow 1 <2>
		/*
		 * Expected: Yellow 1, Blue 1, Wild, Draw four, Yellow skip
		 */

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.analyzePossibleCards(cards[3], Arrays.asList(cards)),
			Arrays.asList(cards[0], cards[2], cards[3], cards[4], cards[5], cards[6], cards[10])));
		// Card: Blue 1 <3>
		/*
		 * Expected: Blue 0, Yellow 1, Blue 1, Wild, Draw four, Blue draw two, Blue reverse
		 */

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.analyzePossibleCards(cards[4], Arrays.asList(cards)), Collections.emptyList()));
		// Card: Wild <4>
		/*
		 * Expected: (none)
		 */

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.analyzePossibleCards(cards[5], Arrays.asList(cards)), Arrays.asList(cards[5])));
		// Card: Draw four (unplayed) <5>
		/*
		 * Expected: Draw four
		 */

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.analyzePossibleCards(cards[6], Arrays.asList(cards)),
			Arrays.asList(cards[6], cards[7])));
		// Card: Blue draw two (unplayed) <6>
		/*
		 * Expected: Blue draw two, Red draw two
		 */

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.analyzePossibleCards(cards[7], Arrays.asList(cards)),
			Arrays.asList(cards[6], cards[7])));
		// Card: Red draw two (unplayed) <7>
		/*
		 * Expected: Blue draw two, Red draw two
		 */

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.analyzePossibleCards(cards[8], Arrays.asList(cards)),
			Arrays.asList(cards[2], cards[4], cards[5], cards[8])));
		// Card: Yellow skip <8>
		/*
		 * Expected: Yellow 1, Wild, Wild draw four, Yellow skip
		 */

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.analyzePossibleCards(cards[9], Arrays.asList(cards)),
			Arrays.asList(cards[4], cards[5], cards[9], cards[10])));
		// Card: Green reverse <9>
		/*
		 * Expected: Wild, Draw four, Green reverse, Blue reverse
		 */

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.analyzePossibleCards(cards[10], Arrays.asList(cards)),
			Arrays.asList(cards[0], cards[3], cards[4], cards[5], cards[6], cards[9], cards[10])));
		// Card: Blue reverse <10>
		/*
		 * Expected: Blue 0, Blue 1, Wild, Draw four, Blue draw two, Green reverse, Blue
		 * reverse
		 */
	}

	@Test
	void testAnalyzeColors() {
		UnoCard[] cards = new UnoCard[] {
				/* Red */ new UnoNumericCard(0, UnoCardColor.RED),
				/* Red */ new UnoNumericCard(1, UnoCardColor.RED),
				/* Red */ new UnoNumericCard(2, UnoCardColor.RED),
				/* Red */ new UnoNumericCard(3, UnoCardColor.RED),
				/* Red */ new UnoNumericCard(4, UnoCardColor.RED),
				// Red x 5

				/* Green */ new UnoNumericCard(0, UnoCardColor.GREEN),
				/* Green */ new UnoNumericCard(1, UnoCardColor.GREEN),
				/* Green */ new UnoNumericCard(2, UnoCardColor.GREEN),
				/* Green */ new UnoNumericCard(3, UnoCardColor.GREEN),
				// Green x 4

				/* Blue */ new UnoNumericCard(0, UnoCardColor.BLUE),
				/* Blue */ new UnoNumericCard(1, UnoCardColor.BLUE),
				/* Blue */ new UnoNumericCard(2, UnoCardColor.BLUE),
				// Blue x 3

				/* Yellow */ new UnoNumericCard(0, UnoCardColor.YELLOW),
				/* Yellow */ new UnoNumericCard(1, UnoCardColor.YELLOW),
				// Yellow x 2

				/* Wild */ new UnoWildCard(),
				// Wild x 1
		};

		List<Entry<Long, UnoCardColor>> colorAnalysis = UnoUtils.analyzeColors(Arrays.asList(cards));

		assertTrue(checkColorAnalysis(colorAnalysis.get(0), 5, UnoCardColor.RED));
		assertTrue(checkColorAnalysis(colorAnalysis.get(1), 4, UnoCardColor.GREEN));
		assertTrue(checkColorAnalysis(colorAnalysis.get(2), 3, UnoCardColor.BLUE));
		assertTrue(checkColorAnalysis(colorAnalysis.get(3), 2, UnoCardColor.YELLOW));
		assertTrue(checkColorAnalysis(colorAnalysis.get(4), 1, UnoCardColor.WILD));
	}

	@Test
	void testGetColorCards() {
		UnoCard[] cards = new UnoCard[] {
				/* 0 Red */ new UnoNumericCard(0, UnoCardColor.RED),
				/* 1 Red */ new UnoNumericCard(1, UnoCardColor.RED),

				/* 2 Green */ new UnoNumericCard(0, UnoCardColor.GREEN),
				/* 3 Green */ new UnoNumericCard(1, UnoCardColor.GREEN),

				/* 4 Blue */ new UnoNumericCard(0, UnoCardColor.BLUE),
				/* 5 Blue */ new UnoNumericCard(1, UnoCardColor.BLUE),

				/* 6 Yellow */ new UnoNumericCard(0, UnoCardColor.YELLOW),
				/* 7 Yellow */ new UnoNumericCard(1, UnoCardColor.YELLOW),

				/* 8 Wild */ new UnoWildCard(),
				/* 9 Wild */ new UnoDrawCard()
		};

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getColorCards(UnoCardColor.RED, Arrays.asList(cards)),
			Arrays.asList(cards[0], cards[1])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getColorCards(UnoCardColor.GREEN, Arrays.asList(cards)),
			Arrays.asList(cards[2], cards[3])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getColorCards(UnoCardColor.BLUE, Arrays.asList(cards)),
			Arrays.asList(cards[4], cards[5])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getColorCards(UnoCardColor.YELLOW, Arrays.asList(cards)),
			Arrays.asList(cards[6], cards[7])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getColorCards(UnoCardColor.WILD, Arrays.asList(cards)),
			Arrays.asList(cards[8], cards[9])));
	}

	@Test
	void testGetActionCards() {
		UnoCard[] cards = new UnoCard[] {
				/* 0 Reverse */ new UnoActionCard(UnoAction.REVERSE, UnoCardColor.RED),
				/* 1 Reverse */ new UnoActionCard(UnoAction.REVERSE, UnoCardColor.GREEN),

				/* 2 Skip */ new UnoActionCard(UnoAction.SKIP, UnoCardColor.BLUE),
				/* 3 Skip */ new UnoActionCard(UnoAction.SKIP, UnoCardColor.YELLOW),

				/* 4 X */ new UnoWildCard(),
				/* 5 X */ new UnoDrawCard()
		};

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getActionCards(UnoAction.REVERSE, Arrays.asList(cards)),
			Arrays.asList(cards[0], cards[1])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getActionCards(UnoAction.SKIP, Arrays.asList(cards)),
			Arrays.asList(cards[2], cards[3])));
	}

	@Test
	void testGetNumberCards() {
		UnoCard[] cards = new UnoCard[] {
				/* 0 3 */ new UnoNumericCard(3, UnoCardColor.RED),
				/* 1 3 */ new UnoNumericCard(3, UnoCardColor.RED),

				/* 2 6 */ new UnoNumericCard(6, UnoCardColor.GREEN),
				/* 3 6 */ new UnoNumericCard(6, UnoCardColor.GREEN),

				/* 4 9 */ new UnoNumericCard(9, UnoCardColor.BLUE),
				/* 5 9 */ new UnoNumericCard(9, UnoCardColor.BLUE),

				/* 6 Wild */ new UnoWildCard(),
				/* 7 Wild */ new UnoDrawCard()
		};

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getNumberCards(3, Arrays.asList(cards)), Arrays.asList(cards[0], cards[1])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getNumberCards(6, Arrays.asList(cards)), Arrays.asList(cards[2], cards[3])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getNumberCards(9, Arrays.asList(cards)), Arrays.asList(cards[4], cards[5])));
	}

	@Test
	void testFilterKind() {
		UnoCard[] cards = new UnoCard[] {
				/* 0 Numeric */ new UnoNumericCard(0, UnoCardColor.RED),
				/* 1 Numeric */ new UnoNumericCard(1, UnoCardColor.GREEN),
				/* 2 Numeric */ new UnoNumericCard(2, UnoCardColor.BLUE),

				/* 3 Action */ new UnoActionCard(UnoAction.REVERSE, UnoCardColor.RED),
				/* 4 Action */ new UnoActionCard(UnoAction.SKIP, UnoCardColor.GREEN),

				/* 5 Draw */ new UnoDrawCard(UnoCardColor.RED),
				/* 6 Draw */ new UnoDrawCard(),

				/* 7 Wild */ new UnoWildCard(),
		};

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.filterKind(UnoNumericCard.class, Arrays.asList(cards)),
			Arrays.asList(cards[0], cards[1], cards[2])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.filterKind(UnoActionCard.class, Arrays.asList(cards)),
			Arrays.asList(cards[3], cards[4])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.filterKind(UnoDrawCard.class, Arrays.asList(cards)),
			Arrays.asList(cards[5], cards[6])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.filterKind(UnoWildCard.class, Arrays.asList(cards)), Arrays.asList(cards[7])));
	}
}
