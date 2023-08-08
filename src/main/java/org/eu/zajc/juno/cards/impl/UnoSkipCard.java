package org.eu.zajc.juno.cards.impl;

import javax.annotation.Nonnull;

import org.eu.zajc.juno.cards.*;

/**
 * A card that skips the next player.
 *
 * @author Marko Zajc
 */
public class UnoSkipCard extends UnoCard {

	/**
	 * Creates a new {@link UnoSkipCard}.
	 *
	 * @param color
	 *            the {@link UnoCardColor}
	 */
	public UnoSkipCard(@Nonnull UnoCardColor color) {
		super(color);
	}

	@Override
	public String toString() {
		return this.getColor() + " skip";
	}

	@Override
	public UnoCard cloneCard() {
		return new UnoSkipCard(getColor());
	}

}
