// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java 
 * Copyright (C) 2019-2023 Marko Zajc
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
package org.eu.zajc.juno.utils;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.eu.zajc.juno.rules.types.UnoCardPlacementRule.PlacementClearance.*;

import java.util.*;

import javax.annotation.Nonnull;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.rules.UnoRule;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;
import org.eu.zajc.juno.rules.types.UnoCardPlacementRule;

/**
 * {@link UnoRule}-specific utilities.
 *
 * @author Marko Zajc
 */
public class UnoRuleUtils {

	private UnoRuleUtils() {}

	/**
	 * Filters a {@link Collection} of {@link UnoCard}s to determine which can be placed
	 * on the {@code target} {@link UnoCard}. The deciding factor here is the
	 * {@link UnoRulePack}, specifically the {@link UnoCardPlacementRule}s in it.
	 *
	 * @param target
	 *            the target (top of the discard) {@link UnoCard}
	 * @param cards
	 *            {@link Collection} of {@link UnoCard}s to filter through
	 * @param pack
	 *            the {@link UnoRulePack} to use
	 * @param hand
	 *            the current {@link UnoHand}
	 *
	 * @return a {@link List} of {@link UnoCard}s that can be placed atop of the
	 *         {@code target} {@link UnoCard}
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static List<UnoCard> combinedPlacementAnalysis(@Nonnull UnoCard target, @Nonnull Collection<UnoCard> cards,
														  @Nonnull UnoRulePack pack, @Nonnull UnoHand hand) {
		var rules = filterRuleKind(pack.getRules(), UnoCardPlacementRule.class);
		var result = new ArrayList<UnoCard>(cards.size());

		for (var card : cards) {
			// Iterates over all cards
			var clearance = rules.stream().map(r -> r.canBePlaced(target, card, hand)).collect(toList());
			// Gets the PlacementClearance-s for this card

			if (clearance.contains(ALLOWED) && !clearance.contains(PROHIBITED))
				result.add(card);
			// Adds the card if allowed
		}

		return result;
	}

	/**
	 * Filters a {@link Collection} of {@link UnoRule}s by their kind.
	 *
	 * @param <T>
	 *            kind of the {@link UnoRule} to search for
	 * @param rules
	 *            {@link Collection} of {@link UnoRule}s to filter
	 * @param kind
	 *            kind of the {@link UnoRule} to search for (a {@link Class}, required by
	 *            Java to cast objects).
	 *
	 * @return a {@link List} containing the requested kind of {@link UnoRule}s
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static <T extends UnoRule> List<T> filterRuleKind(@Nonnull Collection<UnoRule> rules,
															 @Nonnull Class<T> kind) {
		return rules.stream().filter(kind::isInstance).map(kind::cast).collect(toList());
	}

	/**
	 * Finds {@link UnoHouseRule}s in a {@link UnoRulePack}. This will scan the
	 * {@link UnoRule}s of that pack and return all {@link UnoHouseRule} of which
	 * {@link UnoRulePack} share all {@link UnoRule}s.
	 *
	 * @param pack
	 *            the {@link UnoRulePack} to scan
	 *
	 * @return all complete {@link UnoHouseRule}s included in this pack
	 *
	 * @deprecated Use {@link #findHouseRules(UnoRulePack)} instead
	 */
	@Deprecated(since = "2.3", forRemoval = true)
	@Nonnull
	public static List<UnoHouseRule> getHouseRules(UnoRulePack pack) {
		return findHouseRules(pack);
	}

	/**
	 * Finds {@link UnoHouseRule}s in a {@link UnoRulePack}. This will scan the
	 * {@link UnoRule}s of that pack and return all {@link UnoHouseRule} of which
	 * {@link UnoRulePack} share all {@link UnoRule}s.
	 *
	 * @param pack
	 *            the {@link UnoRulePack} to scan
	 *
	 * @return all complete {@link UnoHouseRule}s included in this pack
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static List<UnoHouseRule> findHouseRules(UnoRulePack pack) {
		return asList(UnoHouseRule.values()).stream()
			.filter(hr -> pack.getRules().containsAll(hr.getPack().getRules()))
			.collect(toList());
	}

}
