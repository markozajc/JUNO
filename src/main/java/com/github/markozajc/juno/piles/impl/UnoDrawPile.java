package com.github.markozajc.juno.piles.impl;

import static com.github.markozajc.juno.utils.UnoUtils.filterKind;
import static java.util.Collections.unmodifiableList;

import java.util.*;

import javax.annotation.*;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoNumericCard;
import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.piles.UnoPile;
import com.github.markozajc.juno.utils.UnoGameUtils;

/**
 * A class representing a UNO draw pile. A draw pile behaves as the "entry point" for
 * cards as it is the pile that will be initialized from the {@link UnoDeck}. It is
 * (unlike the {@link UnoDiscardPile}) a FIFO queue that supports polling cards in
 * order, automatically removing from the pile itself whilst doing so.
 *
 * @author Marko Zajc
 */
public class UnoDrawPile implements UnoPile {

	@Nonnull
	private final Queue<UnoCard> cards;
	private boolean initialDrawn;
	@Nonnull
	private Random random;

	/**
	 * Creates a new {@link UnoDrawPile} from a {@link UnoDeck}.
	 *
	 * @param deck
	 *            the {@link UnoDeck} to create this pile from
	 */
	public UnoDrawPile(@Nonnull UnoDeck deck) {
		this(deck.getCards(), new Random(), false);
	}

	/**
	 * Creates a new {@link UnoDrawPile} from a {@link UnoDeck}.
	 *
	 * @param deck
	 *            the {@link UnoDeck} to create this pile from
	 * @param random
	 *            the random number generator used to shuffle the deck on
	 *            {@link #shuffle()}
	 */
	public UnoDrawPile(@Nonnull UnoDeck deck, @Nonnull Random random) {
		this(deck.getCards(), random, false);
	}

	/**
	 * Creates a new {@link UnoDrawPile} from a {@link List} of cards.
	 *
	 * @param cards
	 *            the {@link List} of cards to create this pile from
	 * @param resetAll
	 *            whether to reset all {@link UnoCard}s from the list. It is VERY
	 *            IMPORTANT that this is only set to {@code false} when none of the cards
	 *            have a state (for example cards are a fresh clone from a deck)
	 */
	UnoDrawPile(@Nonnull List<UnoCard> cards, boolean resetAll) {
		this(cards, new Random(), resetAll);
	}

	/**
	 * Creates a new {@link UnoDrawPile} from a {@link List} of cards.
	 *
	 * @param cards
	 *            the {@link List} of cards to create this pile from
	 * @param resetAll
	 *            whether to reset all {@link UnoCard}s from the list. It is VERY
	 *            IMPORTANT that this is only set to {@code false} when none of the cards
	 *            have a state (for example cards are a fresh clone from a deck)
	 * @param random
	 *            the random number generator used to shuffle the deck on
	 *            {@link #shuffle()}
	 *
	 */
	UnoDrawPile(@Nonnull List<UnoCard> cards, @Nonnull Random random, boolean resetAll) {
		this.cards = new ArrayDeque<>(cards);
		this.random = random;

		if (resetAll)
			this.cards.forEach(UnoCard::reset);

		shuffle();
	}

	@Override
	public List<UnoCard> getCards() {
		return new ArrayList<>(this.cards);
	}

	@Override
	public int getSize() {
		return this.cards.size();
	}

	/**
	 * Merges another {@link UnoDrawPile} into this pile, resets all of the cards (from
	 * both s
	 *
	 * @param pile
	 *            the pile to merge
	 */
	public void mergeResetShuffle(UnoDrawPile pile) {
		this.cards.addAll(pile.cards);
		this.cards.forEach(UnoCard::reset);
		shuffle();
	}

	/**
	 * Draws a number of cards from this pile.<br>
	 * <b>CAUTION!</b><br>
	 * This method will not safely draw cards. This means that it will throw an exception
	 * in case there aren't enough cards in it. Use
	 * {@link UnoGameUtils#drawCards(UnoGame, int)} to draw cards instead!
	 *
	 * @param amount
	 *            the amount of {@link UnoCard}s to draw
	 *
	 * @return the drawn {@link UnoCard}
	 *
	 * @throws IllegalStateException
	 *             if there aren't enough cards in this pile to satisfy the requested
	 *             {@code amount}
	 * @throws IllegalArgumentException
	 *             if {@code amount} is negative
	 */
	@Nonnull
	@SuppressWarnings("null")
	public List<UnoCard> draw(@Nonnegative int amount) {
		if (amount < 0)
			throw new IllegalArgumentException("Can't draw less than 0 cards!");

		if (amount > this.getSize())
			throw new IllegalStateException("There aren't enough cards in this pile!");

		var result = new ArrayList<UnoCard>(amount);
		for (int i = 0; i < amount; i++)
			result.add(this.cards.poll());

		return unmodifiableList(result);
	}

	/**
	 * Draws a card from the pile.
	 *
	 * @return the drawn {@link UnoCard}
	 *
	 * @throws IllegalStateException
	 *             if the pile is empty
	 */
	@Nonnull
	@SuppressWarnings("null")
	public UnoCard draw() {
		if (this.cards.isEmpty())
			throw new IllegalStateException("There are no more cards to draw!");

		return this.cards.poll();
	}

	/**
	 * Shuffles the entire pile.
	 */
	public void shuffle() {
		var cardsCopy = new ArrayList<>(getCards());
		Collections.shuffle(cardsCopy, this.random);
		this.cards.clear();
		this.cards.addAll(cardsCopy);
	}

	/**
	 * Draws a card that suits the initial card requirements (means that it can be the
	 * initial card in a discard pile). This means that the card is a
	 * {@link UnoNumericCard}. This method can only be called once for a
	 * {@link UnoDrawPile} and is intended to be called by {@link UnoGame}'s
	 * initialization method.
	 *
	 * @return the initial card
	 */
	public UnoCard drawInitalCard() {
		if (this.initialDrawn)
			throw new IllegalStateException("The initial card has already been drawn!");

		this.initialDrawn = true;

		UnoCard initial = filterKind(UnoNumericCard.class, this.cards).get(0);
		this.cards.remove(initial);
		return initial;
	}
}
