package com.github.markozajc.juno.rules.impl.placement;

import static com.github.markozajc.juno.rules.types.UnoCardPlacementRule.PlacementClearance.*;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoActionCard;
import com.github.markozajc.juno.hands.UnoHand;
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
	 * An action-based placement rule that allows {@link UnoActionCard}s of the same
	 * color to be placed atop of each other and neutrals others.
	 *
	 * @author Marko Zajc
	 */
	public static class ActionPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (target instanceof UnoActionCard && card instanceof UnoActionCard
				&& ((UnoActionCard) target).getFlowAction() == ((UnoActionCard) card).getFlowAction())
				return ALLOWED;
			// Checks whether target's action matches card's action

			return NEUTRAL;
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
