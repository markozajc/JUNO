package com.github.markozajc.juno.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.decks.UnoDeck;

/**
 * A set of utilities meant to be used by {@link UnoDeck} implementations.
 *
 * @author Marko Zajc
 */
public class UnoDeckUtils {

	private UnoDeckUtils() {}

	/**
	 * Clones a {@link Collection} of cards into a modifiable {@link List}. This uses
	 * {@link UnoCard#cloneCard()} to clone the cards, so (if the {@link UnoCard}
	 * implementations implement that method correctly, which they should) the copies of
	 * the {@link UnoCard} aren't be shallow.
	 *
	 * @param cards
	 *            {@link Collection} of {@link UnoCard} to clone
	 * @return modifiable {@link List} of cloned {@link UnoCard}s
	 */
	@Nonnull
	public static List<UnoCard> cloneCards(@Nonnull Collection<UnoCard> cards) {
		List<UnoCard> result = new ArrayList<>();
		for (UnoCard card : cards) {
			result.add(card.cloneCard());
		}

		return result;
	}

}
