package com.github.markozajc.juno.cards.impl;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.*;

/**
 * A card that reverses the flow of the game. In a two-player game, the player is
 * given another turn instead.
 *
 * @author Marko Zajc
 */
public class UnoReverseCard extends UnoCard {

	/**
	 * Creates a new {@link UnoReverseCard}.
	 *
	 * @param color
	 *            the {@link UnoCardColor}
	 */
	public UnoReverseCard(@Nonnull UnoCardColor color) {
		super(color);
	}

	@Override
	public String toString() {
		return this.getColor() + " reverse";
	}

	@Override
	public UnoCard cloneCard() {
		return new UnoReverseCard(getColor());
	}

}
