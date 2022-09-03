package com.github.markozajc.juno.cards.impl;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.*;

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
