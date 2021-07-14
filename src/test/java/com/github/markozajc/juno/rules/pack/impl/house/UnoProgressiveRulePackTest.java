package com.github.markozajc.juno.rules.pack.impl.house;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.cards.*;
import com.github.markozajc.juno.cards.impl.*;
import com.github.markozajc.juno.piles.impl.UnoDiscardPile;

import static org.junit.jupiter.api.Assertions.*;

class UnoProgressiveRulePackTest {

	@Test
	void testGetConsecutive() {
		UnoDiscardPile discard = new UnoDiscardPile();
		// Initializes the discard pile

		discard.add(new UnoDrawCard());
		discard.add(new UnoDrawCard());
		discard.add(new UnoDrawCard());
		discard.add(new UnoDrawCard());
		discard.getCards().forEach(UnoCard::markOpen);
		// Adds 4 open draw four cards

		List<UnoDrawCard> consecutive = UnoProgressiveRulePack.getConsecutive(discard);
		assertEquals(consecutive.size() * consecutive.get(0).getAmount(), 16);
		// That is a consecutive draw of 16 cards

		int drawMark = consecutive.get(0).getAmount();
		for (UnoDrawCard drawCard : consecutive) {
			assertEquals(drawMark, drawCard.getAmount());
			assertTrue(drawCard.isOpen());
		}
		// Checks the relevance of the cards in the returned consecutive draw cards list

		discard.getCards().get(3).markClosed();
		// Sets the bottom card as closed

		consecutive = UnoProgressiveRulePack.getConsecutive(discard);
		assertEquals(consecutive.size() * consecutive.get(0).getAmount(), 12);
		// That is a consecutive draw of 12 cards

		discard.add(new UnoNumericCard(UnoCardColor.RED, 0));
		// Adds an irrelevant color to the top

		consecutive = UnoProgressiveRulePack.getConsecutive(discard);
		assertTrue(consecutive.isEmpty());
		// That is a consecutive draw of 0 cards

		discard.add(new UnoDrawCard(UnoCardColor.RED));
		discard.getTop().markOpen();
		// Adds a single open draw two card

		consecutive = UnoProgressiveRulePack.getConsecutive(discard);
		assertEquals(consecutive.size() * consecutive.get(0).getAmount(), 2);
		// That is a consecutive draw of 2 cards
	}

}
