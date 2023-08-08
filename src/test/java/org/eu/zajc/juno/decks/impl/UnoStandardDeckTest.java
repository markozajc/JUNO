package org.eu.zajc.juno.decks.impl;

import static java.util.Collections.emptyList;
import static org.eu.zajc.juno.TestUtils.getDummyPlayer;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.decks.UnoDeck;
import org.eu.zajc.juno.decks.impl.UnoStandardDeck;
import org.eu.zajc.juno.players.UnoPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnoStandardDeckTest {

	@Test
	void testSize() {
		UnoDeck deck = UnoStandardDeck.getDeck();
		assertEquals(UnoStandardDeck.getExpectedSize(), deck.getCards().size(),
					 "Invalid UNO deck; the standard UNO deck actual size does not match the expected one.");
	}

	@Test
	@SuppressWarnings("null")
	void testCloning() {
		UnoDeck deck = UnoStandardDeck.getDeck();
		UnoPlayer hand = getDummyPlayer(emptyList());
		deck.getCards().get(0).setPlacer(hand);

		UnoCard card = deck.getCards().get(0);
		assertThrows(IllegalStateException.class, card::getPlacer,
					 "getPlacer() doesn't throw, but the placer was never set.");
	}

}
