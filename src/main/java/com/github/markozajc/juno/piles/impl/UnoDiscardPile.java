package com.github.markozajc.juno.piles.impl;

import java.util.*;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.piles.UnoPile;

/**
 * A class representing
 *
 * @author Marko Zajc
 */
public class UnoDiscardPile implements UnoPile {

	@Nonnull
	private final List<UnoCard> cards = new ArrayList<>();

	/**
	 * Marks the top {@link UnoCard} as closed (if possible).
	 *
	 * @deprecated Uses a deprecated way of changing the card, does not let you know
	 *             whether the card has actually been deprecated and requires you
	 *             hardcode in the methods for each {@link UnoCard}. Overall a method you
	 *             shouldn't be using.
	 */
	@Deprecated
	public void markTopPlayed() {
		UnoCard top = getTop();
		if (!(top instanceof UnoDrawCard))
			return;

		((UnoDrawCard) top).setPlayed();
	}

	@Override
	public List<UnoCard> getCards() {
		return this.cards;
	}

	@Override
	public int getSize() {
		return this.cards.size();
	}

	/**
	 * Adds a {@link UnoCard} to the pile. This is used when a hand places (discards) a
	 * {@link UnoCard}.
	 *
	 * @param card
	 *            {@link UnoCard} to discard
	 */
	public void add(UnoCard card) {
		this.cards.add(0, card);
	}

	/**
	 * Adds a {@link List} of {@link UnoCard}s to the pile (only used when initializing
	 * the discard pile).
	 *
	 * @param cards
	 *            {@link List} of {@link UnoCard}s to discard
	 */
	public void addAll(List<UnoCard> cards) {
		this.cards.addAll(0, cards);
	}

	/**
	 * Returns the top card. {@link UnoHand}s must only place cards that are compatible
	 * with this.
	 *
	 * @return the top {@link UnoCard} or {@code null} if this pile is empty (shouldn't
	 *         happen)
	 */
	public UnoCard getTop() {
		if (this.cards.isEmpty())
			return null;

		return this.cards.get(0);
	}

	/**
	 * Creates a {@link UnoDrawPile} from this {@link UnoDiscardPile}. This should
	 * automatically done mid-game by the {@link UnoGame} implementation when the
	 * {@link UnoDrawPile} is all drawn out to refill it. Do note that although this pile
	 * will be emptied when this is called, the top card will remain in order to not
	 * disturb the flow of the game.
	 *
	 * @return a new {@link UnoDrawPile}
	 */
	@SuppressWarnings("null")
	public UnoDrawPile createDrawPile() {
		if (this.cards.isEmpty())
			return new UnoDrawPile(Collections.emptyList(), false);

		UnoCard top = this.cards.remove(0);

		UnoDrawPile pile = new UnoDrawPile(this.cards, true);
		this.cards.clear();
		this.cards.add(top);

		return pile;
	}

	/**
	 * Clears the pile, dereferencing all {@link UnoCard}s from it.
	 */
	public void clear() {
		this.cards.clear();
	}

}
