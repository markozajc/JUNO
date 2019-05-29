package com.github.markozajc.juno.piles.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.TestUtils;
import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.cards.UnoStandardDeck;
import com.github.markozajc.juno.cards.impl.UnoNumericCard;
import com.github.markozajc.juno.decks.UnoDeck;

class UnoDrawPileTest {

	@Test
	void test() {
		UnoDeck deck = new UnoStandardDeck();
		// Defines the deck to use

		UnoDrawPile pile = new UnoDrawPile(deck);
		// Creates a new draw pile from the standard deck

		assertEquals(deck.getExpectedSize(), pile.getSize());
		// Checks whether the deck and the pile are of the same size
	}

	@SuppressWarnings("null")
	@Test
	void testShuffle() {
		UnoDeck deck = TestUtils.getDummyDeck(
			Arrays.asList(new UnoNumericCard(0, UnoCardColor.RED), new UnoNumericCard(1, UnoCardColor.GREEN),
				new UnoNumericCard(2, UnoCardColor.BLUE), new UnoNumericCard(3, UnoCardColor.YELLOW)));
		// Creates a new dummy deck

		UnoDrawPile pile = new UnoDrawPile(deck);
		// Creates a new UnoDrawPile. Cards should be shuffled upon creation.

		assertFalse(deck.getCards().equals(pile.getCards()));
		// Tests the equality of the lists. They shouldn't be equal because one is shuffled
		// and the other is not.
	}

	@Test
	void testDrawInitialCard() {
		UnoDeck deck = new UnoStandardDeck();
		// Defines the deck to use

		UnoDrawPile pile = new UnoDrawPile(deck);
		// Creates a new draw pile from the standard deck

		UnoCard initial = pile.drawInitalCard();
		// Draws the initial card

		assertTrue(initial instanceof UnoNumericCard);
		// Checks the initial card

		assertEquals(pile.getSize(), deck.getExpectedSize() - 1);
		// Checks whether the card has been actually drawn from the pile
	}

}
