package com.github.markozajc.juno.cards;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.players.UnoPlayer;

/**
 * A class representing a card in UNO. The only mandatory thing for a card is a
 * color, which can be one of five colors in {@link UnoCardColor}.
 *
 * @author Marko Zajc
 */
public abstract class UnoCard {

	@Nonnull
	private final UnoCardColor color;
	@Nullable
	private UnoCardColor mask;
	@Nullable
	private UnoPlayer placer;
	private boolean open;

	/**
	 * Creates a new {@link UnoCard}.
	 *
	 * @param color
	 *            this card's color. If this is {@link UnoCardColor#WILD}, you will also
	 *            be able able to set a color mask using
	 *            {@link #setColorMask(UnoCardColor)}
	 */
	public UnoCard(@Nonnull UnoCardColor color) {
		this.color = color;
	}

	/**
	 * @return this {@link UnoCard}'s {@link UnoCardColor} or its color mask in case it
	 *         exists
	 */
	@Nonnull
	public final UnoCardColor getColor() {
		if (this.mask != null)
			return this.mask;

		return this.color;
	}

	/**
	 * Returns this {@link UnoCard}'s "original color", meaning it will never return the
	 * color mask.
	 *
	 * @return this {@link UnoCard}'s {@link UnoCardColor}
	 */
	@Nonnull
	public final UnoCardColor getOriginalColor() {
		return this.color;
	}

	/**
	 * Sets the color mask. A color mask is the color set above the original color and is
	 * returned on {@link #getColor()} (but not on {@link #getOriginalColor()}).
	 *
	 * @param mask
	 *            the new color mask or {@code null} to remove the color mask
	 */
	public final void setColorMask(@Nullable UnoCardColor mask) {
		if (!getOriginalColor().equals(UnoCardColor.WILD))
			throw new IllegalStateException("Card's original color must be \"WILD\" if you want to set a color.");

		this.mask = mask;
	}

	/**
	 * Sets this card's placer - the one placing the card. A placer can only be set once
	 * and should be set when the card is placed to the discard pile.
	 *
	 * @param placer
	 *            this card's placer
	 * @throws IllegalStateException
	 *             in case the placer has already been set.
	 */
	public final void setPlacer(@Nonnull UnoPlayer placer) {
		if (this.placer != null)
			throw new IllegalStateException("This card's placer has already been set.");

		this.placer = placer;
	}

	/**
	 * Returns the card's placer - the {@link UnoHand} that has placed it. This will
	 * throw an {@link IllegalStateException} if the placer has not been set, so be
	 * careful about calling it on cards - only cards that are in the discard pile
	 * (should) have a placer. Neither cards in the draw pile or the ones possessed by
	 * the hands shouldn't have this value set.
	 *
	 * @return this card's placer
	 * @throws IllegalStateException
	 *             in case this card's placer hasn't been set yet
	 */
	@Nonnull
	public UnoPlayer getPlacer() {
		UnoPlayer cardPlacer = this.placer;
		if (cardPlacer == null)
			throw new IllegalStateException("This card's placer hasn't been set yet.");

		return cardPlacer;
	}

	/**
	 * Resets the card's state. The card should be in the same state as if it were just
	 * created from the constructor after this method is called.
	 */
	public void reset() {
		this.mask = null;
		// Unsets the color mask

		this.placer = null;
		// Unsets the placer

		this.open = false;
		// Mark as closed
	}

	public boolean isOpen() {
		return this.open;
	}

	public void markOpen() {
		if (isOpen())
			throw new IllegalStateException("Card is already marked as open");

		this.open = true;
	}

	public void markClosed() {
		if (!isOpen())
			throw new IllegalStateException("Card is already marked as closed");

		this.open = false;
	}

	/**
	 * Creates a clone of the card. The clone should not contain mutable fields (color
	 * mask, placer, etc) but only the immutable ones (original color, etc).
	 *
	 * @return the cloned {@link UnoCard}
	 */
	public abstract UnoCard cloneCard();

}
