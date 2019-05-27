package com.github.markozajc.juno.rules.impl;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule;
import com.github.markozajc.juno.utils.UnoUtils;

/**
 * Placement rules for {@link UnoDrawCard}.
 *
 * @author Marko Zajc
 */
public class DrawPlacementRules {

	private DrawPlacementRules() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = new UnoRulePack(new DrawAmountPlacementRule(), new DrawFourHitchPlacementRule());
	}

	public static class DrawAmountPlacementRule extends UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (target instanceof UnoDrawCard && card instanceof UnoDrawCard
					&& ((UnoDrawCard) target).getAmount() == ((UnoDrawCard) card).getAmount())
				return PlacementClearance.ALLOWED;
			// Checks whether target's amount matches card's amount

			return PlacementClearance.NEUTRAL;
		}
	}

	public static class DrawFourHitchPlacementRule extends UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (card instanceof UnoDrawCard && target.getColor().equals(UnoCardColor.WILD)
					&& !UnoUtils.getColorCards(target.getColor(), hand.getCards()).isEmpty()) {
				return PlacementClearance.PROHIBITED;
			}
			// Prohibits the placement of the wild draw four if the hand possesses a card that
			// has the same color as the target (assumed to be the top of the discard pile) card.

			return PlacementClearance.NEUTRAL;
		}
	}

	/**
	 * @return {@link UnoRulePack} of the official draw placement rules
	 */
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

}
