package com.github.markozajc.juno.rules.pack.impl.house;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.piles.impl.UnoDiscardPile;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.impl.placement.DrawPlacementRules.OpenDrawCardPlacementRule;

public class UnoProgressiveRulePack {

	/**
	 * Checks the relevance of the draw card by the following criteria: (relevance is a
	 * part of the progressive UNO rule and determines whether a draw card is included in
	 * the consecutive draw)
	 * <ul>
	 * <li>the card is a draw card
	 * <li>the draw card has the same amount of cards as the previous ones
	 * <li>the draw card is not closed
	 * </ul>
	 *
	 * @param card
	 *            card to check
	 * @param drawMark
	 *            draw amount on the previous draw cards
	 * @return whether the card is still relevant or not
	 * @see #getConsecutiveDraw()
	 */
	private static boolean isRelevant(UnoCard card, int drawMark) {
		return card instanceof UnoDrawCard && ((UnoDrawCard) card).getAmount() == drawMark && card.isOpen();
	}

	/**
	 * Gets the draw mark of this discard pile. Draw mark is a part of the progressive
	 * UNO rule and determines what cards down the discard pile are relevant to the
	 * consecutive draw. This will return {@code 0} if the top card is not a
	 * {@link UnoDrawCard}.
	 *
	 * @return the draw mark or {@code 0}
	 */
	private int getDrawMark(UnoDiscardPile discard) {
		UnoCard top = discard.getTop();
		if (top instanceof UnoDrawCard) {
			return ((UnoDrawCard) top).getAmount();
		}

		return 0;
	}

	/**
	 * Calculates the <i>consecutive draw</i> for this discard pile. Consecutive draw is
	 * the main point of the progressive UNO rule (which is adopted by JUNO because it's
	 * really great). It's essentially a count of consecutive "relevant" draw cards
	 * (cards of the same type) from top to bottom, multiplied by the <i>draw mark</i>,
	 * which is used in determining relevance and is the amount
	 * ({@link UnoDrawCard#getAmount()}) of the top card
	 * ({@link UnoDiscardPile#getTop()}).<br>
	 * <b>TL;DR</b> consecutive draw is the streak of {@link UnoDrawCard}s.
	 *
	 * @return the consecutive draw or {@code 0} if the top card isn't a
	 *         {@link UnoDrawCard}
	 */
	public int getConsecutiveDraw(UnoDiscardPile discard) {
		int drawMark = getDrawMark(discard);
		if (drawMark == 0)
			return 0;
		// The top card is not a draw card; there's no draw consecutive draw to calculate

		int consecutive = 0;

		for (UnoCard card : discard.getCards()) {
			if (isRelevant(card, drawMark)) {
				consecutive++;
			} else {
				break;
			}
		}
		// Iterates over the draw pile, until it hits an irrelevant card, adding to the
		// consecutive draw on each relevant one

		return consecutive * drawMark;
	}

	public static class ProgressiveUnoPlacementRule extends OpenDrawCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (target instanceof UnoDrawCard && target.isOpen()) {
				if (card instanceof UnoDrawCard
						&& ((UnoDrawCard) card).getAmount() == ((UnoDrawCard) target).getAmount())
					return PlacementClearance.ALLOWED;

				return PlacementClearance.PROHIBITED;
			}

			return PlacementClearance.NEUTRAL;
		}

		@Override
		public ConflictResolution conflictsWith(UnoRule rule) {
			if (rule instanceof OpenDrawCardPlacementRule)
				return ConflictResolution.REPLACE;

			return null;
		}

	}

}
