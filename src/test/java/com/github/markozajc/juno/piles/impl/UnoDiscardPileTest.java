package com.github.markozajc.juno.piles.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.cards.UnoCard;
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
		discard.getCards().forEach(UnoCard::markOpen);
		// Adds 4 open draw four cards

		assertEquals(discard.getConsecutiveDraw(), 16);
		// That is a consecutive draw of 16 cards

		discard.getCards().get(3).markClosed();
		// Sets the bottom card as closed

		assertEquals(discard.getConsecutiveDraw(), 12);
		// That is a consecutive draw of 12 cards

		discard.add(new UnoNumericCard(UnoCardColor.RED, 0));
		// Adds an irrelevant color to the top

		assertEquals(discard.getConsecutiveDraw(), 0);
		// That is a consecutive draw of 0 cards

		discard.add(new UnoDrawCard(UnoCardColor.RED));
		discard.getTop().markOpen();
		// Adds a single open draw two card

		assertEquals(discard.getConsecutiveDraw(), 2);
		// That is a consecutive draw of 2 cards
	}

}
