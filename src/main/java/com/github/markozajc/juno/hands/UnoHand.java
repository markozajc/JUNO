package com.github.markozajc.juno.hands;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.game.UnoGame.UnoPlayer;
import com.github.markozajc.juno.piles.UnoPile;
import com.github.markozajc.juno.piles.impl.UnoDiscardPile;
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
public abstract class UnoHand implements UnoPile {

	/**
	 * Hand's cards
	 */
	@Nonnull
	protected final List<UnoCard> cards = new ArrayList<>();

	@Nonnull
	private final String name;

	/**
	 * Creates a new {@link UnoHand}.
	 *
	 * @param name
	 *            hand's name
	 */
	public UnoHand(@Nonnull String name) {
		this.name = name;
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
	 * Chooses a card to place on top of the discard pile. Just calling this shouldn't
	 * actually change anything (it shouldn't add the card to the discard pile and/or
	 * remove the card from the hand) but should only return a valid {@link UnoCard} that
	 * this hand possesses. Everything else is done by the {@link UnoGame}. Returning
	 * {@code null} here means that the hand wants to draw a card.
	 *
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @return the {@link UnoCard} to play or {@code null} if the hand wants to draw a
	 *         card
	 */
	@Nullable
	public abstract UnoCard playCard(UnoGame game);

	/**
	 * Chooses the {@link UnoCardColor} to set for a wild card.
	 *
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @return a {@link UnoCardColor}
	 */
	@Nonnull
	public abstract UnoCardColor chooseColor(UnoGame game);

	public abstract boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard);

	/**
	 * @return hand's name
	 */
	@Nonnull
	public String getName() {
		return this.name;
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

}
