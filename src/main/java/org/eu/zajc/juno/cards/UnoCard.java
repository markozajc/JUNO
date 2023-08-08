// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java 
 * Copyright (C) 2019-2023 Marko Zajc
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.eu.zajc.juno.cards;

import static org.eu.zajc.juno.cards.UnoCardColor.WILD;

import javax.annotation.*;

import org.eu.zajc.juno.cards.impl.UnoDrawCard;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.players.UnoPlayer;

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
	protected UnoCard(@Nonnull UnoCardColor color) {
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
	 * returned on {@link #getColor()} (but not on {@link #getOriginalColor()}). A color
	 * mask can only be set once and can only be reset with {@link #reset()}. Color masks
	 * can only be applied to cards with the original color of {@link UnoCardColor#WILD}.
	 *
	 * @param mask
	 *            the new color mask
	 *
	 * @throws IllegalStateException
	 *             if this {@link UnoCard}'s {@link #getOriginalColor()} is not
	 *             {@link UnoCardColor#WILD} or if the color mask has already been set
	 */
	public final void setColorMask(@Nullable UnoCardColor mask) {
		if (getOriginalColor() != WILD)
			throw new IllegalStateException("Card's original color must be \"WILD\" if you want to set a color.");

		if (this.mask != null)
			throw new IllegalStateException("Can't set the color mask more than once.");

		this.mask = mask;
	}

	/**
	 * Sets this card's placer - the one placing the card. A placer can only be set once
	 * and should be set when the card is placed to the discard pile.
	 *
	 * @param placer
	 *            this card's placer
	 *
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
	 *
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
		this.placer = null;
		this.open = false;
	}

	/**
	 * Whether the card is "open" or not. A {@link UnoCard} being open can mean multiple
	 * different card-specific things, but it always means that the card has not been
	 * "activated" yet (eg. {@link UnoDrawCard} has been just placed and no cards have
	 * been drawn because of it yet). Cards are usually marked as open
	 * ({@link #markOpen()}) at decision phase and marked as closed
	 * ({@link #markClosed()}) at initialization phase of the next turn.
	 *
	 * @return whether the card is "open" or not
	 */
	public boolean isOpen() {
		return this.open;
	}

	/**
	 * Marks the card as "open" - see {@link #isOpen()} for more details.
	 *
	 * @throws IllegalStateException
	 *             if the {@link UnoCard} is already open
	 *
	 * @see #isOpen()
	 */
	public void markOpen() {
		if (isOpen())
			throw new IllegalStateException("Card is already marked as open");

		this.open = true;
	}

	/**
	 * Marks the {@link UnoCard} as "closed" - see {@link #isOpen()} for more details.
	 *
	 * @throws IllegalStateException
	 *             if the {@link UnoCard} is already closed
	 *
	 * @see #isOpen()
	 */
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
