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

import static org.eu.zajc.juno.rules.types.UnoCardPlacementRule.PlacementClearance.*;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.UnoNumericCard;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.types.UnoCardPlacementRule;

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
				return ALLOWED;
			// Checks whether target's number matches card's number

			return NEUTRAL;
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
