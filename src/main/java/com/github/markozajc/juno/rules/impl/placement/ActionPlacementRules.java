package com.github.markozajc.juno.rules.impl.placement;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoActionCard;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule;

/**
 * Placement rules for {@link UnoActionCard}.
 *
 * @author Marko Zajc
 */
public class ActionPlacementRules {

	private ActionPlacementRules() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = new UnoRulePack(new ActionPlacementRule());
	}

	/**
	 * An action-based placement rule that allows {@link UnoActionCard}s of the same color to be placed
	 * atop of each other and neutrals others.
	 *
	 * @author Marko Zajc
	 */
	public static class ActionPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoPlayer player) {
			if (target instanceof UnoActionCard && card instanceof UnoActionCard
					&& ((UnoActionCard) target).getAction().equals(((UnoActionCard) card).getAction()))
				return PlacementClearance.ALLOWED;
			// Checks whether target's action matches card's action

			return PlacementClearance.NEUTRAL;
		}

	}

	/**
	 * @return {@link UnoRulePack} of the official action placement rules
	 */
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

}
