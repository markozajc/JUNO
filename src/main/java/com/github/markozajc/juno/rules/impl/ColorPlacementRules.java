package com.github.markozajc.juno.rules.impl;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule;

public class ColorPlacementRules {

	private ColorPlacementRules() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = new UnoRulePack(new ColorPlacementRule(), new WildColorPlacementRule());
	}

	/**
	 * A color-based placement rule that allows cards of the same color to be placed atop
	 * of each other
	 *
	 * @author Marko Zajc
	 */
	public static class ColorPlacementRule extends UnoCardPlacementRule {

		@Override
		public boolean canBePlaced(UnoCard target, UnoCard card) {
			return target.getColor().equals(card.getColor());
		}

	}

	/**
	 * A color-based placement rule that allows a wild card to be placed atop of anything
	 *
	 * @author Marko Zajc
	 */
	public static class WildColorPlacementRule extends UnoCardPlacementRule {

		@Override
		public boolean canBePlaced(UnoCard target, UnoCard card) {
			return target.getColor().equals(UnoCardColor.WILD);
		}

	}

	/**
	 * @return {@link UnoRulePack} of the official color placement rules
	 */
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

}
