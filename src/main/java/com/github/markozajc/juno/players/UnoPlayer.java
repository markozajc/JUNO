package com.github.markozajc.juno.players;

import java.util.List;

import javax.annotation.*;

import com.github.markozajc.juno.cards.*;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.players.impl.*;

/**
 * A class representing a player in UNO. A UNO player owns {@link UnoHand}s and
 * contains the logic that determines what card is to be placed when it gets a turn
 * ({@link #playCard(UnoGame, UnoPlayer)}) whether or not it should place a
 * newly-drawn card ({@link #shouldPlayDrawnCard(UnoGame, UnoCard, UnoPlayer)}) and
 * what color to assign to wild cards ({@link #chooseColor(UnoGame)}). A
 * {@link UnoPlayer} can either be autonomous (an example of that would be
 * {@link UnoStrategicPlayer}) or controlled by a human being (for example
 * {@link UnoStreamPlayer} and {@link UnoConsolePlayer}).
 *
 * @author Marko Zajc
 */
public abstract class UnoPlayer {

	@Nonnull
	private UnoHand hand;
	@Nonnull
	private final String name;

	/**
	 * Creates a new {@link UnoPlayer}.
	 *
	 * @param name
	 *            this {@link UnoPlayer}'s name
	 */
	protected UnoPlayer(@Nonnull String name) {
		this.hand = new UnoHand();
		this.name = name;
	}

	/**
	 * @return the {@link UnoHand} this player possesses
	 */
	@Nonnull
	public final UnoHand getHand() {
		return this.hand;
	}

	/**
	 * @return this player's name
	 */
	@Nonnull
	public final String getName() {
		return this.name;
	}

	/**
	 * Sets this player's {@link UnoHand}, changing its entire inventory of cards. Take
	 * care not to ruin the card economy while using this!
	 *
	 * @param hand
	 *            the new {@link UnoHand}.
	 */
	public final void setHand(@Nonnull UnoHand hand) {
		this.hand = hand;
	}

	/**
	 * A shortcut to {@link UnoHand#getCards()}.
	 *
	 * @return this {@link UnoPlayer}'s cards
	 */
	public final List<UnoCard> getCards() {
		return getHand().getCards();
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
	 *
	 * @return the {@link UnoCard} to place or {@code null} if the hand wants to draw a
	 *         card
	 */
	@Nullable
	public abstract UnoCard playCard(UnoGame game);

	/**
	 * Chooses the {@link UnoCardColor} to set for a wild card.
	 *
	 * @param game
	 *            the ongoing {@link UnoGame}
	 *
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
	 *
	 * @return whether the card should be placed
	 */
	public abstract boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard, UnoPlayer next);

}
