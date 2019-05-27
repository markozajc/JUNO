package com.github.markozajc.juno.rules.impl;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule;

public class DrawPlacementRules {

	private DrawPlacementRules() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = new UnoRulePack(/* TODO */);
	}

	public class DrawAmountPlacementRule extends UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (target instanceof UnoDrawCard && card instanceof UnoDrawCard
					&& ((UnoDrawCard) target).getAmount() == ((UnoDrawCard) card).getAmount())
				return PlacementClearance.ALLOWED;
			// Checks whether target's amount matches card's amount

			return PlacementClearance.NEUTRAL;
		}
	}

	public class DrawFourHitchPlacementRule extends UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (target instanceof UnoDrawCard && card instanceof UnoDrawCard) {
				// TODO
			}

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
