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

import org.eu.zajc.juno.cards.*;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.types.UnoCardPlacementRule;

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
			if (target.getColor() == card.getColor())
				return ALLOWED;

			return NEUTRAL;
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
			if (card.getOriginalColor() == WILD)
				return ALLOWED;

			return NEUTRAL;
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
