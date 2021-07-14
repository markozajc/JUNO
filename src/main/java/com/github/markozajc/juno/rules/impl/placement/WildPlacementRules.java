package com.github.markozajc.juno.rules.impl.placement;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoWildCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule;

/**
 * Placement rules for {@link UnoWildCard}.
 *
 * @author Marko Zajc
 *
 * @deprecated Already defined in {@link ColorPlacementRules}
 */
@Deprecated
public class WildPlacementRules {

	private WildPlacementRules() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = new UnoRulePack(new WildPlacementRule());
	}

	/**
	 * The only official rule for the {@link UnoWildCard} - the wild card can be placed
	 * on literally anything.
	 *
	 * @author Marko Zajc
	 */
	public static class WildPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (card instanceof UnoWildCard)
				return PlacementClearance.ALLOWED;
			// Checks whether card is a wild card

			return PlacementClearance.NEUTRAL;
		}
	}

	/**
	 * @return {@link UnoRulePack} of the official wild card placement rules
	 */
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

}
