package org.eu.zajc.juno.hands;

import static org.eu.zajc.juno.cards.UnoCardColor.*;

import org.eu.zajc.juno.cards.impl.UnoNumericCard;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.piles.impl.UnoDiscardPile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnoHandTest {

	@SuppressWarnings("null")
	@Test
	void testAddToDiscard() {
		UnoHand hand = new UnoHand();
		UnoNumericCard card = new UnoNumericCard(RED, 0);
		hand.getCards().add(card);
		// Adds a card to the hand

		UnoDiscardPile discard = new UnoDiscardPile();
		// Creates a new discard pile

		assertTrue(hand.addToDiscard(discard, hand.getCards().get(0)));
		// Adds a card from the hand

		assertTrue(hand.getCards().isEmpty());
		assertEquals(card, discard.getTop());
		// Tests the situation

		assertFalse(hand.addToDiscard(discard, card.cloneCard()));
		// Tests the nonexistent card block
	}

	@Test
	void testClear() {
		UnoHand hand = new UnoHand();

		hand.getCards().add(new UnoNumericCard(RED, 0));
		hand.getCards().add(new UnoNumericCard(BLUE, 1));
		hand.getCards().add(new UnoNumericCard(GREEN, 2));
		hand.getCards().add(new UnoNumericCard(YELLOW, 3));
		// Adds a some cards to the hand

		assertEquals(4, hand.getSize());
		// Tests the hand size

		hand.clear();
		// Clears the hand

		assertEquals(0, hand.getSize());
		// Tests the hand size
	}

}
