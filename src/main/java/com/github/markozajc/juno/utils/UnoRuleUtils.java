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
		List<UnoCardPlacementRule> rules = filterRuleKind(pack.getRules(), UnoCardPlacementRule.class);
		return cards.stream() /* 1 */
				.filter(c -> rules.stream()
						/* 2 */.map(r -> r.canBePlaced(target, c, null))
						/* 3 */.filter(v -> v)
						/* 4 */.count() > 0/* 5 */)
				.collect(Collectors.toList()); /* 6 */
		// Lambda magic
		// Basically iterates over the cards (1), then iterates over the placement rules for
		// each card (2), maps all rules' return values to a stream of booleans (3), filters
		// out the falses (4) and checks if there are any elements in the filtered stream
		// (5), then collects the stream into a list (6)
	}

	@SuppressWarnings("null")
	@Nonnull
	public static <T extends UnoRule> List<T> filterRuleKind(@Nonnull List<UnoRule> rules, @Nonnull Class<T> kind) {
		return rules.stream().filter(kind::isInstance).map(kind::cast).collect(Collectors.toList());
	}

}
