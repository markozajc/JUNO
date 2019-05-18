package com.mz.uno.piles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


class UnoStandardDeckTest {

	@Test
	void testSize() {
		assertEquals(UnoStandardDeck.getExpectedSize(), UnoStandardDeck.getCards().size(), "Invalid UNO deck; the standard UNO deck must contain 108 cards.");
	}

}
