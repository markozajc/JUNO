package com.github.markozajc.juno.decks.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.cards.UnoStandardDeck;
import com.github.markozajc.juno.decks.UnoDeck;


class UnoStandardDeckTest {

	@Test
	void testSize() {
		UnoDeck deck = new UnoStandardDeck();
		assertEquals(deck.getExpectedSize(), deck.getCards().size(), "Invalid UNO deck; the standard UNO deck actual size does not match the expected one.");
	}

}
