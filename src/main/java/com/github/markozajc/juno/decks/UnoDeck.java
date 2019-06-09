package com.github.markozajc.juno.decks;

import java.util.List;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.piles.UnoPile;
import com.github.markozajc.juno.utils.UnoDeckUtils;

/**
 * A class representing a UNO deck. UNO decks are taken and taken at the beginning of
 * a UNO round and cloned. The clone {@link List} of the {@link UnoCard} is then
 * distributed among the {@link UnoPile}s.
 *
 * @author Marko Zajc
 */
public class UnoDeck {

	@Nonnull
	private final List<UnoCard> cards;

	/**
	 * Creates a new {@link UnoDeck}.
	 *
	 * @param cards
	 *            a {@link List} of {@link UnoCard} this {@link UnoDeck} should consist
	 *            of
	 */
	public UnoDeck(@Nonnull List<UnoCard> cards) {
		this.cards = cards;
	}

	/**
	 * Returns a modifiable clone of the deck.
	 *
	 * @return a clone of the deck
	 */
	@Nonnull
	public List<UnoCard> getCards() {
		return UnoDeckUtils.cloneCards(this.cards);
	}

}
