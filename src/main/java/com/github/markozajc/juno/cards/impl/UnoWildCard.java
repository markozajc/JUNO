package com.github.markozajc.juno.cards.impl;

import com.github.markozajc.juno.cards.*;

/**
 * A card that can be placed onto anything and allows its placer to change it to any
 * of the four other colors (red, green, blue and yellow).
 *
 * @author Marko Zajc
 */
public class UnoWildCard extends UnoCard {

	/**
	 * Creates a new {@link UnoWildCard}.
	 */
	public UnoWildCard() {
		super(UnoCardColor.WILD);
	}

	@Override
	public String toString() {
		return getOriginalColor().toString();
	}

	@Override
	public UnoCard cloneCard() {
		return new UnoWildCard();
	}

}
