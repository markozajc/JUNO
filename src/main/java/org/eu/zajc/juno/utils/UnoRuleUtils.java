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
package org.eu.zajc.juno.utils;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.eu.zajc.juno.rules.types.UnoCardPlacementRule.PlacementClearance.*;

import java.util.*;

import javax.annotation.*;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.rules.UnoRule;
import org.eu.zajc.juno.rules.impl.placement.DrawPlacementRules.DrawFourHitchPlacementRule;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;
import org.eu.zajc.juno.rules.types.UnoCardPlacementRule;
import org.eu.zajc.juno.rules.types.UnoCardPlacementRule.PlacementClearance;

/**
 * {@link UnoRule}-specific utilities.
 *
 * @author Marko Zajc
 */
public class UnoRuleUtils {

	/**
	 * Determines which {@link UnoCard} from a {@link Collection} can be placed on top of
	 * the {@code target} {@link UnoCard}. The deciding factor here is the
	 * {@link UnoRulePack}, specifically the {@link UnoCardPlacementRule}s in it.
	 *
	 * @param target
	 *            the target (top of the discard) {@link UnoCard}
	 * @param cards
	 *            {@link Collection} of {@link UnoCard}s to dry-run placement for
	 * @param rules
	 *            the {@link UnoRulePack} to use
	 * @param hand
	 *            the current {@link UnoHand}
	 *
	 * @return a {@link List} of {@link UnoCard}s that can be placed on top of the
	 *         {@code target} {@link UnoCard}
	 *
	 * @see #getProhibitingRule(UnoCard, UnoCard, UnoRulePack, UnoHand)
	 * @see #getProhibitingRules(UnoCard, UnoCard, UnoRulePack, UnoHand)
	 * @see #canPlaceCard(UnoCard, UnoCard, UnoRulePack, UnoHand)
	 *
	 * @deprecated This method has been renamed to
	 *             {@link #getPlaceableCards(UnoCard, Collection, UnoRulePack, UnoHand)}
	 */
	@Deprecated(since = "2.4", forRemoval = true)
	@Nonnull
	public static List<UnoCard> combinedPlacementAnalysis(@Nonnull UnoCard target, @Nonnull Collection<UnoCard> cards,
														  @Nonnull UnoRulePack rules, @Nonnull UnoHand hand) {
		return getPlaceableCards(target, cards, rules, hand);
	}

	/**
	 * Determines whether a {@link UnoCard} can be placed on top of the the
	 * {@code target} {@link UnoCard}. The deciding factor here is the
	 * {@link UnoRulePack}, specifically the {@link UnoCardPlacementRule}s in it.
	 *
	 * @param target
	 *            the target (top of the discard) {@link UnoCard}
	 * @param card
	 *            the {@link UnoCard} to dry-run placement for
	 * @param rules
	 *            the {@link UnoRulePack} to use
	 * @param hand
	 *            the current {@link UnoHand}
	 *
	 * @return {@code true} if the card can be placed on top of the {@code target}
	 *         {@link UnoCard}, meaning at least one rule returns
	 *         {@link PlacementClearance#ALLOWED} and none return
	 *         {@link PlacementClearance#PROHIBITED}
	 *
	 * @see #getProhibitingRule(UnoCard, UnoCard, UnoRulePack, UnoHand)
	 * @see #getProhibitingRules(UnoCard, UnoCard, UnoRulePack, UnoHand)
	 * @see #getPlaceableCards(UnoCard, Collection, UnoRulePack, UnoHand)
	 */
	public static boolean canPlaceCard(@Nonnull UnoCard target, @Nonnull UnoCard card, @Nonnull UnoRulePack rules,
									   @Nonnull UnoHand hand) {
		boolean allowed = false;
		for (var rule : filterRuleKind(rules.getRules(), UnoCardPlacementRule.class)) {
			var clearance = rule.canBePlaced(target, card, hand);
			if (clearance == PROHIBITED)
				return false;
			else if (clearance == ALLOWED)
				allowed = true;
		}
		return allowed;
	}

	/**
	 * Determines which {@link UnoCard} from a {@link Collection} can be placed on top of
	 * the {@code target} {@link UnoCard}. The deciding factor here is the
	 * {@link UnoRulePack}, specifically the {@link UnoCardPlacementRule}s in it.
	 *
	 * @param target
	 *            the target (top of the discard) {@link UnoCard}
	 * @param cards
	 *            {@link Collection} of {@link UnoCard}s to dry-run placement for
	 * @param rules
	 *            the {@link UnoRulePack} to use
	 * @param hand
	 *            the current {@link UnoHand}
	 *
	 * @return a {@link List} of {@link UnoCard}s that can be placed on top of the
	 *         {@code target} {@link UnoCard}
	 *
	 * @see #getProhibitingRules(UnoCard, UnoCard, UnoRulePack, UnoHand)
	 * @see #getProhibitingRule(UnoCard, UnoCard, UnoRulePack, UnoHand)
	 * @see #canPlaceCard(UnoCard, UnoCard, UnoRulePack, UnoHand)
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static List<UnoCard> getPlaceableCards(@Nonnull UnoCard target, @Nonnull Collection<UnoCard> cards,
												  @Nonnull UnoRulePack rules, @Nonnull UnoHand hand) {
		return cards.stream().filter(c -> canPlaceCard(target, c, rules, hand)).collect(toList());
	}

	/**
	 * Finds a list of {@link UnoCardPlacementRule}s that prohibit placement.<br>
	 * <br>
	 * <b>Note:</b> an empty list does not by itself mean that placement is allowed, only
	 * that none of the rules prohibit it. This can be used as feedback to the player
	 * when explaining why placement is not allowed for lesser-known rules, for example
	 * {@link DrawFourHitchPlacementRule}.
	 *
	 * @param target
	 *            the target (top of the discard) {@link UnoCard}
	 * @param card
	 *            the {@link UnoCard} to dry-run placement for
	 * @param rules
	 *            the {@link UnoRulePack} to use
	 * @param hand
	 *            the current {@link UnoHand}
	 *
	 * @return a {@link List} of {@link UnoCardPlacementRule}s that prohibit placement
	 *
	 * @see #getProhibitingRule(UnoCard, UnoCard, UnoRulePack, UnoHand)
	 * @see #getPlaceableCards(UnoCard, Collection, UnoRulePack, UnoHand)
	 * @see #canPlaceCard(UnoCard, UnoCard, UnoRulePack, UnoHand)
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static List<UnoCardPlacementRule> getProhibitingRules(@Nonnull UnoCard target, @Nonnull UnoCard card,
																 @Nonnull UnoRulePack rules, @Nonnull UnoHand hand) {
		return filterRuleKind(rules.getRules(), UnoCardPlacementRule.class).stream().filter(r -> {
			return r.canBePlaced(target, card, hand) == PROHIBITED;
		}).collect(toList());
	}

	/**
	 * Finds any {@link UnoCardPlacementRule} that prohibits placement.<br>
	 * <br>
	 * <b>Note:</b> an empty list does not by itself mean that placement is allowed, only
	 * that none of the rules prohibit it. This can be used as feedback to the player
	 * when explaining why placement is not allowed for lesser-known rules, for example
	 * {@link DrawFourHitchPlacementRule}.
	 *
	 * @param target
	 *            the target (top of the discard) {@link UnoCard}
	 * @param card
	 *            the {@link UnoCard} to dry-run placement for
	 * @param rules
	 *            the {@link UnoRulePack} to use
	 * @param hand
	 *            the current {@link UnoHand}
	 *
	 * @return any {@link UnoCardPlacementRule} that prohibits placement or {@code null}
	 *         if there are none
	 *
	 * @see #getProhibitingRule(UnoCard, UnoCard, UnoRulePack, UnoHand)
	 * @see #getPlaceableCards(UnoCard, Collection, UnoRulePack, UnoHand)
	 * @see #canPlaceCard(UnoCard, UnoCard, UnoRulePack, UnoHand)
	 */
	@Nullable
	public static UnoCardPlacementRule getProhibitingRule(@Nonnull UnoCard target, @Nonnull UnoCard card,
														  @Nonnull UnoRulePack rules, @Nonnull UnoHand hand) {
		for (var rule : filterRuleKind(rules.getRules(), UnoCardPlacementRule.class)) {
			if (rule.canBePlaced(target, card, hand) == PROHIBITED)
				return rule;
		}
		return null;
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
	 * @param rules
	 *            the {@link UnoRulePack} to scan
	 *
	 * @return all complete {@link UnoHouseRule}s included in this pack
	 *
	 * @deprecated Use {@link #findHouseRules(UnoRulePack)} instead
	 */
	@Deprecated(since = "2.3", forRemoval = true)
	@Nonnull
	public static List<UnoHouseRule> getHouseRules(UnoRulePack rules) {
		return findHouseRules(rules);
	}

	/**
	 * Finds {@link UnoHouseRule}s in a {@link UnoRulePack}. This will scan the
	 * {@link UnoRule}s of that pack and return all {@link UnoHouseRule} of which
	 * {@link UnoRulePack} share all {@link UnoRule}s.
	 *
	 * @param rules
	 *            the {@link UnoRulePack} to scan
	 *
	 * @return all complete {@link UnoHouseRule}s included in this pack
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static List<UnoHouseRule> findHouseRules(UnoRulePack rules) {
		return asList(UnoHouseRule.values()).stream()
			.filter(hr -> rules.getRules().containsAll(hr.getPack().getRules()))
			.collect(toList());
	}

	private UnoRuleUtils() {}

}
