package com.github.markozajc.juno.cards.impl;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.*;

/**
 * A card that comes in two variations and changes the flow of the game and comes in
 * two variations:
 * <ul>
 * <li>Reverse reverses the player order
 * <li>Skip skips the next player
 * </ul>
 * In a two-player game both of the cards give the player placing them another turn.
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
