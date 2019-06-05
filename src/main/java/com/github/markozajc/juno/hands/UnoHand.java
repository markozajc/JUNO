package com.github.markozajc.juno.hands;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.piles.UnoPile;
import com.github.markozajc.juno.piles.impl.UnoDiscardPile;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.utils.UnoGameUtils;

/**
 * A representation of a hand - the thing that holds and places cards to the discard
 * pile. Each {@link UnoPlayer} has a hand. Hands can either be automated (meaning
 * they decide for themselves) or human-driven (meaning they're an interface that
 * lets a human decide). A hand must do two essential things; choose colors and
 * cards. It is requested to choose a color each time it places a wild-colored card.
 * It is requested to choose (play) a card each time it gets a turn. It may not
 * choose invalid cards (cards that it either doesn't possess or that can't be placed
 * on the discard pile) or invalid colors (the wild color).
 *
 * @author Marko Zajc
 */
public class UnoHand implements UnoPile {

	/**
	 * Hand's cards
	 */
	@Nonnull
	protected final List<UnoCard> cards = new ArrayList<>();

	@Override
	public List<UnoCard> getCards() {
		return this.cards;
	}

	@Override
	public int getSize() {
		return this.cards.size();
	}

	/**
	 * Draws an amount of cards from the draw pile in the given {@link UnoGame} (this
	 * will also add the cards to the {@link UnoHand}).
	 *
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @param amount
	 *            the amount of cards to draw
	 * @return the drawn cards
	 */
	@Nonnull
	public final List<UnoCard> draw(@Nonnull UnoGame game, @Nonnegative int amount) {
		List<UnoCard> drawnCards = UnoGameUtils.drawCards(game, amount);
		this.cards.addAll(drawnCards);
		return drawnCards;
	}

	/**
	 * Adds a {@link UnoCard} from this {@link UnoHand} to a {@link UnoDiscardPile} and
	 * removes it from the {@link UnoHand}. The return value here determines whether the
	 * {@link UnoHand} actually possesses the given {@link UnoCard} or not. The
	 * {@link UnoCard} will also not be added to the {@link UnoDiscardPile} in case the
	 * {@link UnoHand} doesn't possess it.
	 *
	 * @param discard
	 *            the {@link UnoDiscardPile} to discard the {@link UnoCard} to
	 * @param card
	 *            the {@link UnoCard} to discard
	 * @return whether the {@link UnoHand} actually possesses the given {@link UnoCard}
	 */
	public final boolean addToDiscard(@Nonnull UnoDiscardPile discard, @Nonnull UnoCard card) {
		if (!this.cards.remove(card))
			return false;

		discard.add(card);

		return true;
	}

	/**
	 * Clears the {@link UnoHand}, dereferencing all {@link UnoCard}s from it.
	 */
	public final void clear() {
		this.cards.clear();
	}

}
