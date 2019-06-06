package com.github.markozajc.juno.cards.impl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.players.UnoPlayer;

/**
 * A card that makes the other player draw two or four cards, depending on the
 * specified amount. There are several specifications when it comes to draw cards:
 * <ul>
 * <li>The only allowed amounts are two and four
 * <li>The draw four card is always wild
 * <li>The draw two card can be of any color, except for wild
 * <li>There are 4 draw four cards in a standard UNO deck
 * <li>There are 8 draw two cards in a standard UNO deck, two for each color (except
 * for wild)
 * </ul>
 *
 * @author Marko Zajc
 */
public class UnoDrawCard extends UnoCard {

	private final int amount;

	/**
	 * Creates a new draw two card.
	 *
	 * @param color
	 *            card's color
	 * @throws IllegalArgumentException
	 *             if {@code color} is equal to {@link UnoCardColor#WILD}
	 */
	public UnoDrawCard(@Nonnull UnoCardColor color) {
		this(color, 2);

		if (color.equals(UnoCardColor.WILD))
			throw new IllegalArgumentException("The wild card color is not for draw two cards!");
	}

	/**
	 * Creates a new draw four card.
	 */
	public UnoDrawCard() {
		this(UnoCardColor.WILD, 4);
	}

	private UnoDrawCard(@Nonnull UnoCardColor color, @Nonnegative int amount) {
		super(color);
		this.amount = amount;
	}

	/**
	 * Marks this card as "closed" ("played"). This means that a player has already drawn because of
	 * it.
	 *
	 * @deprecated You shouldn't be using this directly as
	 *             {@link #drawTo(UnoGame, UnoPlayer)} already does it for you.
	 */
	@Deprecated
	public void setPlayed() {
		markClosed();
	}

	/**
	 * The "draw amount" of this card. {@link UnoDrawCard} supports draw four and draw
	 * two cards so this is either {@code 2} or {@code 4}.
	 *
	 * @return the draw amount of this cards
	 */
	public int getAmount() {
		return this.amount;
	}

	@Override
	public String toString() {
		return getOriginalColor().toString() + " draw " + this.getAmount();
	}

	/**
	 * Draws the set amount of {@link UnoCard}s from the draw pile of the given
	 * {@link UnoGame} and adds them to a {@link UnoHand}. This method is safe as it uses
	 * {@link UnoHand#draw(UnoGame, int)}.
	 *
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @param player
	 *            the owner of the {@link UnoHand} to add the drawn cards to
	 * @throws IllegalStateException
	 *             in case this card is already marked as closed
	 */
	public void drawTo(@Nonnull UnoGame game, @Nonnull UnoPlayer player) {
		markClosed();
		player.getHand().draw(game, getAmount());
	}

	@Override
	public UnoCard cloneCard() {
		return new UnoDrawCard(getColor(), getAmount());
	}

}
