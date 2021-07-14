package com.github.markozajc.juno.decks.impl;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.TestUtils;
import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.players.UnoPlayer;

import static org.junit.jupiter.api.Assertions.*;

class UnoStandardDeckTest {

	@Test
	void testSize() {
		UnoDeck deck = UnoStandardDeck.getDeck();
		assertEquals(UnoStandardDeck.getExpectedSize(), deck.getCards().size(),
					 "Invalid UNO deck; the standard UNO deck actual size does not match the expected one.");
	}

	@SuppressWarnings("null")
	@Test
	void testCloning() {
		UnoDeck deck = UnoStandardDeck.getDeck();
		UnoPlayer hand = TestUtils.getDummyPlayer(Collections.emptyList());
		deck.getCards().get(0).setPlacer(hand);

		assertThrows(IllegalStateException.class, () -> deck.getCards().get(0).getPlacer());
	}

}
