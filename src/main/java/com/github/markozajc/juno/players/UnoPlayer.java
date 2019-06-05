package com.github.markozajc.juno.players;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;

public abstract class UnoPlayer {

	@Nonnull
	private UnoHand hand;
	@Nonnull
	private final String name;

	public UnoPlayer(@Nonnull String name) {
		this.hand = new UnoHand();
		this.name = name;
	}

	@Nonnull
	public UnoHand getHand() {
		return this.hand;
	}

	@Nonnull
	public String getName() {
		return this.name;
	}

	public void setHand(@Nonnull UnoHand hand) {
		this.hand = hand;
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
	 * @param next
	 *            the next {@link UnoPlayer}
	 * @return the {@link UnoCard} to play or {@code null} if the hand wants to draw a
	 *         card
	 */
	@Nullable
	public abstract UnoCard playCard(UnoGame game, UnoPlayer next);

	/**
	 * Chooses the {@link UnoCardColor} to set for a wild card.
	 *
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @return a {@link UnoCardColor}
	 */
	@Nonnull
	public abstract UnoCardColor chooseColor(UnoGame game);

	/**
	 * Lets the hand decide whether to place the just-drawn {@link UnoCard} or not. This
	 * method will only be called if the card can actually be placed so there's no need
	 * to check that.
	 *
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @param drawnCard
	 *            the just-drawn {@link UnoCard}
	 * @param next
	 *            the next {@link UnoPlayer}
	 * @return whether the card should be placed
	 */
	public abstract boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard, UnoPlayer next);

}
