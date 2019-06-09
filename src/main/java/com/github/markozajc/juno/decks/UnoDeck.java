package com.github.markozajc.juno.decks;

import java.util.List;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.utils.UnoDeckUtils;

/**
 * A class representing a (static) UNO deck. Note that the output of any of the
 * methods is not expected to change under any circumstance and is considered as such
 * by the UnoGame implementations.
 *
 * @author Marko Zajc
 */
public class UnoDeck {

	@Nonnull
	private final List<UnoCard> cards;

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
