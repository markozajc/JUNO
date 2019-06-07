package com.github.markozajc.juno.rules.pack.impl.house;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.impl.placement.DrawPlacementRules.OpenDrawCardPlacementRule;

public class UnoProgressiveRulePack {

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
