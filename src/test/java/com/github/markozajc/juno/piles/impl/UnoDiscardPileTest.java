package com.github.markozajc.juno.piles.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.cards.impl.UnoNumericCard;

class UnoDiscardPileTest {

	@Test
	void testGetConsecutiveDraw() {
		UnoDiscardPile discard = new UnoDiscardPile();
		// Initializes the discard pile

		discard.add(new UnoDrawCard());
		discard.add(new UnoDrawCard());
		discard.add(new UnoDrawCard());
		discard.add(new UnoDrawCard());
		// Adds 4 draw four cards

		// That is a consecutive draw of 16 cards
		assertEquals(discard.getConsecutiveDraw(), 16);

		// Sets the bottom card as played
		((UnoDrawCard) discard.getCards().get(3)).setPlayed();

		// That is a consecutive draw of 12 cards
		assertEquals(discard.getConsecutiveDraw(), 12);

		discard.add(new UnoNumericCard(UnoCardColor.RED, 0));
		// Adds an irrelevant color to the top

		// That is a consecutive draw of 0 cards
		assertEquals(discard.getConsecutiveDraw(), 0);

		discard.add(new UnoDrawCard(UnoCardColor.RED));
		// Adds a single draw two card

		// That is a consecutive draw of 2 cards
		assertEquals(discard.getConsecutiveDraw(), 2);
	}

}
