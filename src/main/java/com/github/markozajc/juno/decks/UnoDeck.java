package com.github.markozajc.juno.decks;

import java.util.List;

import javax.annotation.Nonnegative;
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
public interface UnoDeck {

	/**
	 * Should return a <u>CLONE</u> of the deck. You can acquire a clone of the cards
	 * using {@link UnoDeckUtils#cloneCards(java.util.Collection)}.
	 *
	 * @return a clone of the deck
	 */
	@Nonnull
	public List<UnoCard> getCards();

	/**
	 * The expected size of the deck. This is encouraged to be a hard-coded constant, but
	 * make sure it equals {@link #getCards()}'s size.
	 *
	 * @return this deck's size
	 */
	@Nonnegative
	public int getExpectedSize();

}
