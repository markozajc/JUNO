package com.github.markozajc.juno.decks.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.TestUtils;
import com.github.markozajc.juno.cards.UnoStandardDeck;
import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.players.UnoPlayer;

class UnoStandardDeckTest {

	@Test
	void testSize() {
		UnoDeck deck = new UnoStandardDeck();
		assertEquals(deck.getExpectedSize(), deck.getCards().size(),
			"Invalid UNO deck; the standard UNO deck actual size does not match the expected one.");
	}

	@SuppressWarnings("null")
	@Test
	void testCloning() {
		UnoDeck deck = new UnoStandardDeck();
		UnoPlayer hand = TestUtils.getDummyPlayer(TestUtils.getDummyHand(Collections.emptyList()));
		deck.getCards().get(0).setPlacer(hand);

		assertThrows(IllegalStateException.class, () -> deck.getCards().get(0).getPlacer());
	}

}
