package com.github.markozajc.juno.rules.impl;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule;

/**
 * {@link UnoCardColor}-based rules for all {@link UnoCard}s.
 *
 * @author Marko Zajc
 */
public class ColorPlacementRules {

	private ColorPlacementRules() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = new UnoRulePack(new ColorPlacementRule(), new WildColorPlacementRule());
	}

	/**
	 * A color-based placement rule that allows cards of the same color to be placed atop
	 * of each other and neutrals others.
	 *
	 * @author Marko Zajc
	 */
	public static class ColorPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (target.getColor().equals(card.getColor()))
				return PlacementClearance.ALLOWED;

			return PlacementClearance.NEUTRAL;
		}

	}

	/**
	 * A color-based placement rule that allows a wild card to be placed atop of anything
	 * and neutrals others.
	 *
	 * @author Marko Zajc
	 */
	public static class WildColorPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (card.getColor().equals(UnoCardColor.WILD))
				return PlacementClearance.ALLOWED;

			return PlacementClearance.NEUTRAL;
		}

	}

	/**
	 * @return {@link UnoRulePack} of the official color placement rules.
	 */
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

}
