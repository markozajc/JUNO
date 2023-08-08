package org.eu.zajc.juno.rules.impl.placement;

import static org.eu.zajc.juno.rules.types.UnoCardPlacementRule.PlacementClearance.*;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.*;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.types.UnoCardPlacementRule;

/**
 * Placement rules for {@link UnoSkipCard}.
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
	 * An action-based placement rule that allows {@link UnoSkipCard}s and
	 * {@link UnoReverseCard}s of the same color to be placed atop of each other and
	 * neutrals others.
	 *
	 * @author Marko Zajc
	 */
	public static class ActionPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if ((target instanceof UnoSkipCard && card instanceof UnoSkipCard) || (target instanceof UnoReverseCard && card instanceof UnoReverseCard))
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
