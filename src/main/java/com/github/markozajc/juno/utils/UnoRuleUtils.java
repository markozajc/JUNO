package com.github.markozajc.juno.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule;

public class UnoRuleUtils {

	private UnoRuleUtils() {}

	@SuppressWarnings("null")
	@Nonnull
	public static List<UnoCard> combinedPlacementAnalysis(@Nonnull UnoCard target, @Nonnull Collection<UnoCard> cards, @Nonnull UnoRulePack pack) {
		return filterRuleKind(pack.getRules(), UnoCardPlacementRule.class).stream()
				.flatMap(r -> r.analyzePossiblePlacements(target, cards).stream())
				.distinct()
				.collect(Collectors.toList());
		// Lambda magic
	}

	@SuppressWarnings("null")
	@Nonnull
	public static <T extends UnoRule> List<T> filterRuleKind(@Nonnull List<UnoRule> rules, @Nonnull Class<T> kind) {
		return rules.stream().filter(kind::isInstance).map(kind::cast).collect(Collectors.toList());
	}

}
