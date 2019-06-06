package com.github.markozajc.juno.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule.PlacementClearance;

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
	 * @return a {@link List} of {@link UnoCard}s that can be placed atop of the
	 *         {@code target} {@link UnoCard}
	 */
	@SuppressWarnings("null")
	@Nonnull
	public static List<UnoCard> combinedPlacementAnalysis(@Nonnull UnoCard target, @Nonnull Collection<UnoCard> cards, @Nonnull UnoRulePack pack, @Nonnull UnoHand hand) {
		List<UnoCardPlacementRule> rules = filterRuleKind(pack.getRules(), UnoCardPlacementRule.class);
		List<UnoCard> result = new ArrayList<>();

		for (UnoCard card : cards) {
			// Iterates over all cards
			List<PlacementClearance> clearance = rules.stream()
					.map(r -> r.canBePlaced(target, card, hand))
					.collect(Collectors.toList());
			// Gets the PlacementClearance-s for this card

			if (clearance.contains(PlacementClearance.ALLOWED) && !clearance.contains(PlacementClearance.PROHIBITED))
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
	 * @return a {@link List} containing the requested kind of {@link UnoRule}s
	 */
	@SuppressWarnings("null")
	@Nonnull
	public static <T extends UnoRule> List<T> filterRuleKind(@Nonnull Collection<UnoRule> rules, @Nonnull Class<T> kind) {
		return rules.stream().filter(kind::isInstance).map(kind::cast).collect(Collectors.toList());
	}

	@SuppressWarnings("null")
	@Nonnull
	public static List<UnoHouseRule> getHouseRules(UnoRulePack pack) {
		return Arrays.asList(UnoHouseRule.values())
				.stream()
				.filter(hr -> pack.getRules().containsAll(hr.getPack().getRules()))
				.collect(Collectors.toList());
	}

}
