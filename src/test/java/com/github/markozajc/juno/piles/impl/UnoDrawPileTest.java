package com.github.markozajc.juno.piles.impl;

import static com.github.markozajc.juno.cards.UnoCardColor.*;
import static java.util.Arrays.asList;

import java.util.Random;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.*;
import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.decks.impl.UnoStandardDeck;

import static org.junit.jupiter.api.Assertions.*;

class UnoDrawPileTest {

	@Test
	void testCreation() {
		UnoDeck deck = UnoStandardDeck.getDeck();
		// Defines the deck to use

		UnoDrawPile pile = new UnoDrawPile(deck, new Random(0) /* determinism */);
		// Creates a new draw pile from the standard deck

		assertEquals(UnoStandardDeck.getExpectedSize(), pile.getSize(),
					 "Size of deck and pile differs, most likely due to a deck copying failure.");
		// Checks whether the deck and the pile are of the same size
	}

	@Test
	void testShuffle() {
		@SuppressWarnings("null")
		UnoDeck deck =
			new UnoDeck(asList(new UnoNumericCard(RED, 0), new UnoNumericCard(GREEN, 1), new UnoNumericCard(BLUE, 2),
							   new UnoNumericCard(YELLOW, 3), new UnoNumericCard(RED, 4), new UnoNumericCard(GREEN, 5),
							   new UnoNumericCard(BLUE, 6), new UnoNumericCard(YELLOW, 7), new UnoNumericCard(RED, 8),
							   new UnoNumericCard(GREEN, 9)));
		// Creates a new dummy deck

		UnoDrawPile pile = new UnoDrawPile(deck, new Random(0) /* determinism */);
		// Creates a new UnoDrawPile. Cards should be shuffled upon creation.

		assertNotEquals(deck.getCards(), pile.getCards(), "The resulting deck is not shuffled.");
		// Tests the equality of the lists. They shouldn't be equal because one is shuffled
		// and the other is not.
	}

	@Test
	void testDrawInitialCard() {
		@SuppressWarnings("null")
		UnoDeck deck = new UnoDeck(asList(new UnoWildCard(), new UnoNumericCard(RED, 0)));
		// Defines the deck to use

		UnoDrawPile pile = new UnoDrawPile(deck, new Random(0) /* determinism, and we get the wild card first */);
		// Creates a new draw pile from the standard deck

		UnoCard initial = pile.drawInitalCard();
		// Draws the initial card

		assertTrue(initial instanceof UnoNumericCard, "The initial card is not a UnoNumericCard");
		// Checks the initial card

		assertEquals(1, pile.getSize(), "The size of the pile has not been reduced by one after drawing a card.");
		// Checks whether the card has been actually drawn from the pile
	}

}
