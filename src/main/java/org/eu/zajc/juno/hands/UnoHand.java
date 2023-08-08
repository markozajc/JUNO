package org.eu.zajc.juno.hands;

import static org.eu.zajc.juno.utils.UnoGameUtils.drawCards;

import java.util.*;

import javax.annotation.*;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.piles.UnoPile;
import org.eu.zajc.juno.piles.impl.UnoDiscardPile;
import org.eu.zajc.juno.players.UnoPlayer;

/**
 * A representation of a hand - a container for {@link UnoCard}s that is owned by a
 * {@link UnoPlayer}. {@link UnoHand}s manage the cards and {@link UnoPlayer}s
 * contain the logic.
 *
 * @author Marko Zajc
 *
 * @see UnoPlayer
 */
public class UnoHand implements UnoPile {

	/**
	 * Hand's cards
	 */
	@Nonnull
	protected final List<UnoCard> cards = new ArrayList<>();

	@Nonnull
	@Override
	public List<UnoCard> getCards() {
		return this.cards; // TODO return an unmodifiable list
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
	 *
	 * @return the drawn cards
	 */
	@Nonnull
	public final List<UnoCard> draw(@Nonnull UnoGame game, @Nonnegative int amount) {
		var drawnCards = drawCards(game, amount);
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
	 *
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
