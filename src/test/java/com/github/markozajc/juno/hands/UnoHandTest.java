package com.github.markozajc.juno.hands;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.cards.impl.UnoNumericCard;
import com.github.markozajc.juno.piles.impl.UnoDiscardPile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnoHandTest {

	@SuppressWarnings("null")
	@Test
	void testAddToDiscard() {
		UnoHand hand = new UnoHand();
		UnoNumericCard card = new UnoNumericCard(UnoCardColor.RED, 0);
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

		hand.getCards().add(new UnoNumericCard(UnoCardColor.RED, 0));
		hand.getCards().add(new UnoNumericCard(UnoCardColor.BLUE, 1));
		hand.getCards().add(new UnoNumericCard(UnoCardColor.GREEN, 2));
		hand.getCards().add(new UnoNumericCard(UnoCardColor.YELLOW, 3));
		// Adds a some cards to the hand

		assertEquals(4, hand.getSize());
		// Tests the hand size

		hand.clear();
		// Clears the hand

		assertEquals(0, hand.getSize());
		// Tests the hand size
	}

}
