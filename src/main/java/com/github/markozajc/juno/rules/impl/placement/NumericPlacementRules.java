package com.github.markozajc.juno.rules.impl.placement;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoNumericCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule;

/**
 * Placement rules for {@link UnoNumericCard}.
 *
 * @author Marko Zajc
 */
public class NumericPlacementRules {

	private NumericPlacementRules() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = new UnoRulePack(new NumericPlacementRule());
	}

	/**
	 * A number-based placement rule that allows {@link UnoNumericCard}s of the same
	 * number to be placed atop of each other and neutrals others.
	 *
	 * @author Marko Zajc
	 */
	public static class NumericPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (target instanceof UnoNumericCard && card instanceof UnoNumericCard
				&& ((UnoNumericCard) target).getNumber() == ((UnoNumericCard) card).getNumber())
				return PlacementClearance.ALLOWED;
			// Checks whether target's number matches card's number

			return PlacementClearance.NEUTRAL;
		}

	}

	/**
	 * @return {@link UnoRulePack} of the official number placement rules
	 */
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

}
