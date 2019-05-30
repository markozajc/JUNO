package com.github.markozajc.juno.cards.impl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;

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

	private final int draw;
	private boolean played = false;

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

	private UnoDrawCard(@Nonnull UnoCardColor color, @Nonnegative int draw) {
		super(color);
		this.draw = draw;
	}

	@Override
	public boolean isPlayed() {
		return this.played;
	}

	/**
	 * Marks this card as "played". This means that a player has already drawn because of
	 * it.
	 */
	public void setPlayed() {
		this.played = true;
	}

	/**
	 * The "draw amount" of this card. {@link UnoDrawCard} supports draw four and draw
	 * two cards so this is either {@code 2} or {@code 4}.
	 *
	 * @return the draw amount of this cards
	 */
	public int getAmount() {
		return this.draw;
	}

	@Override
	public String toString() {
		return getOriginalColor().toString() + " draw " + this.draw;
	}

	@Override
	public void reset() {
		super.reset();

		this.played = false;
	}

}
