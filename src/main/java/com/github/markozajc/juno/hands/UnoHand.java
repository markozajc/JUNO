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
	 * @param drawn
	 *            whether this hand has drawn a card in this turn. This method can
	 *            actually be called twice in a turn; first time to let it do something
	 *            and second time in case the hand has requested to draw and has drawn a
	 *            card that it can place on top of the discard pile
	 * @return the {@link UnoCard} to play or {@code null} if the hand wants to draw a
	 *         card
	 */
	@Nullable
	public abstract UnoCard playCard(UnoGame game, boolean drawn);

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
	 * @return hand's name
	 */
	@Nonnull
	public String getName() {
		return this.name;
	}

}
