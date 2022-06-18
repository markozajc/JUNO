package com.github.markozajc.juno.rules.pack.impl.house;

import static com.github.markozajc.juno.cards.UnoCardColor.RED;
import static com.github.markozajc.juno.rules.pack.impl.house.UnoProgressiveRulePack.getConsecutive;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.cards.UnoCard;
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

		List<UnoDrawCard> consecutive = getConsecutive(discard);
		assertEquals(16, consecutive.size() * consecutive.get(0).getAmount(),
					 "4 draw four cards do not produce a consecutive draw of 16");
		// That is a consecutive draw of 16 cards

		int drawMark = consecutive.get(0).getAmount();
		for (UnoDrawCard drawCard : consecutive) {
			assertEquals(drawMark, drawCard.getAmount());
			assertTrue(drawCard.isOpen());
		}
		// Checks the relevance of the cards in the returned consecutive draw cards list

		discard.getCards().get(3).markClosed();
		// Sets the bottom card as closed

		consecutive = getConsecutive(discard);
		assertEquals(12, consecutive.size() * consecutive.get(0).getAmount(),
					 "3 draw four cards do not produce a consecutive draw of 16");
		// That is a consecutive draw of 12 cards

		discard.add(new UnoNumericCard(RED, 0));
		// Adds an irrelevant color to the top

		consecutive = getConsecutive(discard);
		assertTrue(consecutive.isEmpty());
		// That is a consecutive draw of 0 cards

		discard.add(new UnoDrawCard(RED));
		discard.getTop().markOpen();
		// Adds a single open draw two card

		consecutive = getConsecutive(discard);
		assertEquals(2, consecutive.size() * consecutive.get(0).getAmount(),
					 "1 draw two cards do not produce a consecutive draw of 2");
		// That is a consecutive draw of 2 cards
	}

}
