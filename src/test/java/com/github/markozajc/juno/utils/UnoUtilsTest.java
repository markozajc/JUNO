package com.github.markozajc.juno.utils;

import java.util.*;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.TestUtils;
import com.github.markozajc.juno.cards.*;
import com.github.markozajc.juno.cards.impl.*;
import com.github.markozajc.juno.cards.impl.UnoActionCard.UnoAction;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UnoUtilsTest {

	private static boolean checkColorAnalysis(Entry<Long, UnoCardColor> entry, long expectedQuantity,
											  UnoCardColor expectedColor) {
		System.out.println("[========= TESTING ANALYSIS =========]");
		System.out.println("<quantity> " + entry.getKey() + " : " + expectedQuantity);
		System.out.println("< color  > " + entry.getValue() + " : " + expectedColor);

		return entry.getKey() == expectedQuantity && entry.getValue().equals(expectedColor);
	}

	@Test
	void testAnalyzeColors() {
		UnoCard[] cards = new UnoCard[] { /* Red */ new UnoNumericCard(UnoCardColor.RED, 0),
			/* Red */ new UnoNumericCard(UnoCardColor.RED, 1), /* Red */ new UnoNumericCard(UnoCardColor.RED, 2),
			/* Red */ new UnoNumericCard(UnoCardColor.RED, 3), /* Red */ new UnoNumericCard(UnoCardColor.RED, 4),
			// Red x 5

			/* Green */ new UnoNumericCard(UnoCardColor.GREEN, 0),
			/* Green */ new UnoNumericCard(UnoCardColor.GREEN, 1),
			/* Green */ new UnoNumericCard(UnoCardColor.GREEN, 2),
			/* Green */ new UnoNumericCard(UnoCardColor.GREEN, 3),
			// Green x 4

			/* Blue */ new UnoNumericCard(UnoCardColor.BLUE, 0), /* Blue */ new UnoNumericCard(UnoCardColor.BLUE, 1),
			/* Blue */ new UnoNumericCard(UnoCardColor.BLUE, 2),
			// Blue x 3

			/* Yellow */ new UnoNumericCard(UnoCardColor.YELLOW, 0),
			/* Yellow */ new UnoNumericCard(UnoCardColor.YELLOW, 1),
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
		UnoCard[] cards = new UnoCard[] { /* 0 Red */ new UnoNumericCard(UnoCardColor.RED, 0),
			/* 1 Red */ new UnoNumericCard(UnoCardColor.RED, 1),

			/* 2 Green */ new UnoNumericCard(UnoCardColor.GREEN, 0),
			/* 3 Green */ new UnoNumericCard(UnoCardColor.GREEN, 1),

			/* 4 Blue */ new UnoNumericCard(UnoCardColor.BLUE, 0),
			/* 5 Blue */ new UnoNumericCard(UnoCardColor.BLUE, 1),

			/* 6 Yellow */ new UnoNumericCard(UnoCardColor.YELLOW, 0),
			/* 7 Yellow */ new UnoNumericCard(UnoCardColor.YELLOW, 1),

			/* 8 Wild */ new UnoWildCard(), /* 9 Wild */ new UnoDrawCard() };

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
		UnoCard[] cards = new UnoCard[] { /* 0 Reverse */ new UnoActionCard(UnoCardColor.RED, UnoAction.REVERSE),
			/* 1 Reverse */ new UnoActionCard(UnoCardColor.GREEN, UnoAction.REVERSE),

			/* 2 Skip */ new UnoActionCard(UnoCardColor.BLUE, UnoAction.SKIP),
			/* 3 Skip */ new UnoActionCard(UnoCardColor.YELLOW, UnoAction.SKIP),

			/* 4 X */ new UnoWildCard(), /* 5 X */ new UnoDrawCard() };

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getActionCards(UnoAction.REVERSE, Arrays.asList(cards)),
												 Arrays.asList(cards[0], cards[1])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getActionCards(UnoAction.SKIP, Arrays.asList(cards)),
												 Arrays.asList(cards[2], cards[3])));
	}

	@Test
	void testGetNumberCards() {
		UnoCard[] cards = new UnoCard[] { /* 0 3 */ new UnoNumericCard(UnoCardColor.RED, 3),
			/* 1 3 */ new UnoNumericCard(UnoCardColor.RED, 3),

			/* 2 6 */ new UnoNumericCard(UnoCardColor.GREEN, 6), /* 3 6 */ new UnoNumericCard(UnoCardColor.GREEN, 6),

			/* 4 9 */ new UnoNumericCard(UnoCardColor.BLUE, 9), /* 5 9 */ new UnoNumericCard(UnoCardColor.BLUE, 9),

			/* 6 Wild */ new UnoWildCard(), /* 7 Wild */ new UnoDrawCard() };

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getNumberCards(3, Arrays.asList(cards)),
												 Arrays.asList(cards[0], cards[1])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getNumberCards(6, Arrays.asList(cards)),
												 Arrays.asList(cards[2], cards[3])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.getNumberCards(9, Arrays.asList(cards)),
												 Arrays.asList(cards[4], cards[5])));
	}

	@Test
	void testFilterKind() {
		UnoCard[] cards = new UnoCard[] { /* 0 Numeric */ new UnoNumericCard(UnoCardColor.RED, 0),
			/* 1 Numeric */ new UnoNumericCard(UnoCardColor.GREEN, 1),
			/* 2 Numeric */ new UnoNumericCard(UnoCardColor.BLUE, 2),

			/* 3 Action */ new UnoActionCard(UnoCardColor.RED, UnoAction.REVERSE),
			/* 4 Action */ new UnoActionCard(UnoCardColor.GREEN, UnoAction.SKIP),

			/* 5 Draw */ new UnoDrawCard(UnoCardColor.RED), /* 6 Draw */ new UnoDrawCard(),

			/* 7 Wild */ new UnoWildCard(), };

		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.filterKind(UnoNumericCard.class, Arrays.asList(cards)),
												 Arrays.asList(cards[0], cards[1], cards[2])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.filterKind(UnoActionCard.class, Arrays.asList(cards)),
												 Arrays.asList(cards[3], cards[4])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.filterKind(UnoDrawCard.class, Arrays.asList(cards)),
												 Arrays.asList(cards[5], cards[6])));
		assertTrue(TestUtils.listEqualsUnordered(UnoUtils.filterKind(UnoWildCard.class, Arrays.asList(cards)),
												 Arrays.asList(cards[7])));
	}
}
