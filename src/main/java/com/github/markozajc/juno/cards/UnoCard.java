package com.github.markozajc.juno.cards;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
	 * Resets the card's state. The card should be in the same state as if it were just
	 * created from the constructor after this method is called.
	 */
	public void reset() {
		if (getOriginalColor().equals(UnoCardColor.WILD))
			setColorMask(null);
		// Unsets the color mask (if possible)
	}

	/**
	 * Whether this card has been "played" yet. A played card means that its action has
	 * been executed.
	 *
	 * @return this card's state
	 */
	public boolean isPlayed() {
		return true;
	}

}
