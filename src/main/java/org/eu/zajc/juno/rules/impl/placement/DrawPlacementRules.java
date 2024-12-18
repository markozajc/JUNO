// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java
 * Copyright (C) 2019-2024 Marko Zajc
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.eu.zajc.juno.rules.impl.placement;

import static org.eu.zajc.juno.cards.UnoCardColor.WILD;
import static org.eu.zajc.juno.rules.types.UnoCardPlacementRule.PlacementClearance.*;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.UnoDrawCard;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.types.UnoCardPlacementRule;
import org.eu.zajc.juno.utils.UnoUtils;

/**
 * Placement rules for {@link UnoDrawCard}.
 *
 * @author Marko Zajc
 */
public class DrawPlacementRules {

	private DrawPlacementRules() {}

	private static UnoRulePack rules;

	private static void createPack() {
		rules = new UnoRulePack(new DrawAmountPlacementRule(), new DrawFourHitchPlacementRule(),
								new OpenDrawCardPlacementRule());
	}

	/**
	 * An amount-based placement rule that allows {@link UnoDrawCard} of the same amount
	 * to be placed on each other.
	 *
	 * @author Marko Zajc
	 */
	public static class DrawAmountPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (target instanceof UnoDrawCard && card instanceof UnoDrawCard
				&& ((UnoDrawCard) target).getAmount() == ((UnoDrawCard) card).getAmount())
				return ALLOWED;
			// Checks whether target's amount matches card's amount

			return NEUTRAL;
		}
	}

	/**
	 * The extra color-based "hitch" to the draw four; prohibits placement of wild draw
	 * four on a card in case its holder has a hand of the same color as that card
	 * (doesn't apply to wild-colored cards).
	 *
	 * @author Marko Zajc
	 */
	public static class DrawFourHitchPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (card instanceof UnoDrawCard && target.getOriginalColor() != WILD
				&& ((UnoDrawCard) card).getAmount() == 4
				&& !UnoUtils.getColorCards(target.getOriginalColor(), hand.getCards()).isEmpty())
				return PROHIBITED;
			// Prohibits the placement of the wild draw four if the hand possesses a card that
			// has the same color as the target (assumed to be the top of the discard pile) card.

			return NEUTRAL;
		}
	}

	/**
	 * A placement rule that prohibits all cards from being placed on an open
	 * {@link UnoDrawCard}.
	 *
	 * @author Marko Zajc
	 */
	public static class OpenDrawCardPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (target instanceof UnoDrawCard && target.isOpen())
				return PROHIBITED;

			return NEUTRAL;
		}

	}

	/**
	 * @return {@link UnoRulePack} of the official draw placement rules
	 */
	public static UnoRulePack getPack() {
		if (rules == null)
			createPack();

		return rules;
	}

}
