package com.github.markozajc.juno.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule.PlacementClearance;

public class UnoRuleUtils {

	private UnoRuleUtils() {}

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

	@SuppressWarnings("null")
	@Nonnull
	public static <T extends UnoRule> List<T> filterRuleKind(@Nonnull List<UnoRule> rules, @Nonnull Class<T> kind) {
		return rules.stream().filter(kind::isInstance).map(kind::cast).collect(Collectors.toList());
	}

}
