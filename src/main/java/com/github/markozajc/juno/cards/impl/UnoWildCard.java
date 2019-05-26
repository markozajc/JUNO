package com.github.markozajc.juno.cards.impl;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;

/**
 * A card that can be placed onto anything and allows its placer to change it to any
 * of the four other colors (red, green, blue and yellow).
 *
 * @author Marko Zajc
 */
public class UnoWildCard implements UnoCard {

	private UnoCardColor color = UnoCardColor.WILD;

	@Override
	public UnoCardColor getColor() {
		return this.color;
	}

	@Override
	public String toString() {
		return "Wild";
	}

	/**
	 * Sets this wild card's color.
	 *
	 * @param color
	 *            the new color
	 */
	public void setColor(UnoCardColor color) {
		this.color = color;
	}

	@Override
	public void reset() {
		this.color = UnoCardColor.WILD;
	}

}
