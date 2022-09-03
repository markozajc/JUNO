package com.github.markozajc.juno.utils;

import static com.github.markozajc.juno.TestUtils.listEqualsUnordered;
import static com.github.markozajc.juno.cards.UnoCardColor.*;
import static com.github.markozajc.juno.utils.UnoUtils.*;
import static java.lang.System.out;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.cards.*;
import com.github.markozajc.juno.cards.impl.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UnoUtilsTest {

	private static boolean checkColorAnalysis(Entry<Long, UnoCardColor> entry, long expectedQuantity,
											  UnoCardColor expectedColor) {
		out.println("[========= TESTING ANALYSIS =========]");
		out.println("<quantity> " + entry.getKey() + " : " + expectedQuantity);
		out.println("< color  > " + entry.getValue() + " : " + expectedColor);

		return entry.getKey() == expectedQuantity && entry.getValue().equals(expectedColor);
	}

	@Test
	void testAnalyzeColors() {
		UnoCard[] cards = new UnoCard[] {
			/* Red */ new UnoNumericCard(RED, 0),
			/* Red */ new UnoNumericCard(RED, 1),
			/* Red */ new UnoNumericCard(RED, 2),
			/* Red */ new UnoNumericCard(RED, 3),
			/* Red */ new UnoNumericCard(RED, 4),
			// Red x 5

			/* Green */ new UnoNumericCard(GREEN, 0),
			/* Green */ new UnoNumericCard(GREEN, 1),
			/* Green */ new UnoNumericCard(GREEN, 2),
			/* Green */ new UnoNumericCard(GREEN, 3),
			// Green x 4

			/* Blue */ new UnoNumericCard(BLUE, 0),
			/* Blue */ new UnoNumericCard(BLUE, 1),
			/* Blue */ new UnoNumericCard(BLUE, 2),
			// Blue x 3

			/* Yellow */ new UnoNumericCard(YELLOW, 0),
			/* Yellow */ new UnoNumericCard(YELLOW, 1),
			// Yellow x 2

			/* Wild */ new UnoWildCard(),
			// Wild x 1
		};

		List<Entry<Long, UnoCardColor>> colorAnalysis = analyzeColors(asList(cards));

		assertTrue(checkColorAnalysis(colorAnalysis.get(0), 5, RED));
		assertTrue(checkColorAnalysis(colorAnalysis.get(1), 4, GREEN));
		assertTrue(checkColorAnalysis(colorAnalysis.get(2), 3, BLUE));
		assertTrue(checkColorAnalysis(colorAnalysis.get(3), 2, YELLOW));
		assertTrue(checkColorAnalysis(colorAnalysis.get(4), 1, WILD));
	}

	@Test
	void testGetColorCards() {
		UnoCard[] cards = new UnoCard[] {
			/* 0 Red */ new UnoNumericCard(RED, 0),
			/* 1 Red */ new UnoNumericCard(RED, 1),

			/* 2 Green */ new UnoNumericCard(GREEN, 0),
			/* 3 Green */ new UnoNumericCard(GREEN, 1),

			/* 4 Blue */ new UnoNumericCard(BLUE, 0),
			/* 5 Blue */ new UnoNumericCard(BLUE, 1),

			/* 6 Yellow */ new UnoNumericCard(YELLOW, 0),
			/* 7 Yellow */ new UnoNumericCard(YELLOW, 1),

			/* 8 Wild */ new UnoWildCard(),
			/* 9 Wild */ new UnoDrawCard() };

		assertTrue(listEqualsUnordered(getColorCards(RED, asList(cards)), asList(cards[0], cards[1])));
		assertTrue(listEqualsUnordered(getColorCards(GREEN, asList(cards)), asList(cards[2], cards[3])));
		assertTrue(listEqualsUnordered(getColorCards(BLUE, asList(cards)), asList(cards[4], cards[5])));
		assertTrue(listEqualsUnordered(getColorCards(YELLOW, asList(cards)), asList(cards[6], cards[7])));
		assertTrue(listEqualsUnordered(getColorCards(WILD, asList(cards)), asList(cards[8], cards[9])));
	}

	@Test
	void testGetNumberCards() {
		UnoCard[] cards = new UnoCard[] {
			/* 0 3 */ new UnoNumericCard(RED, 3),
			/* 1 3 */ new UnoNumericCard(RED, 3),

			/* 2 6 */ new UnoNumericCard(GREEN, 6),
			/* 3 6 */ new UnoNumericCard(GREEN, 6),

			/* 4 9 */ new UnoNumericCard(BLUE, 9),
			/* 5 9 */ new UnoNumericCard(BLUE, 9),

			/* 6 Wild */ new UnoWildCard(),
			/* 7 Wild */ new UnoDrawCard() };

		assertTrue(listEqualsUnordered(getNumberCards(3, asList(cards)), asList(cards[0], cards[1])));
		assertTrue(listEqualsUnordered(getNumberCards(6, asList(cards)), asList(cards[2], cards[3])));
		assertTrue(listEqualsUnordered(getNumberCards(9, asList(cards)), asList(cards[4], cards[5])));
	}

	@Test
	void testFilterKind() {
		UnoCard[] cards = new UnoCard[] {
			/* 0 Numeric */ new UnoNumericCard(RED, 0),
			/* 1 Numeric */ new UnoNumericCard(GREEN, 1),
			/* 2 Numeric */ new UnoNumericCard(BLUE, 2),

			/* 3 Action */ new UnoReverseCard(RED),
			/* 4 Action */ new UnoSkipCard(GREEN),

			/* 5 Draw */ new UnoDrawCard(RED),
			/* 6 Draw */ new UnoDrawCard(),

			/* 7 Wild */ new UnoWildCard(), };

		assertTrue(listEqualsUnordered(filterKind(UnoNumericCard.class, asList(cards)),
									   asList(cards[0], cards[1], cards[2])));
		assertTrue(listEqualsUnordered(filterKind(UnoReverseCard.class, asList(cards)), asList(cards[3])));
		assertTrue(listEqualsUnordered(filterKind(UnoSkipCard.class, asList(cards)), asList(cards[4])));
		assertTrue(listEqualsUnordered(filterKind(UnoDrawCard.class, asList(cards)), asList(cards[5], cards[6])));
		assertTrue(listEqualsUnordered(filterKind(UnoWildCard.class, asList(cards)), asList(cards[7])));
	}
}
