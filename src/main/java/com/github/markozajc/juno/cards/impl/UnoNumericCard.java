package com.github.markozajc.juno.cards.impl;

import javax.annotation.*;

import com.github.markozajc.juno.cards.*;

/**
 * A class representing a numeric card in UNO. There are 2 cards for each number from
 * 1 to 9 for each color and 1 zero card for each color in a standard UNO deck. Wild
 * numeric cards do not exist and are thus not supported by this class. Cards with
 * numbers not in the range of 0-9 also don't exist and are not supported.
 *
 * @author Marko Zajc
 */
public class UnoNumericCard extends UnoCard {

	private final int number;

	/**
	 * Creates a new {@link UnoNumericCard}.
	 *
	 * @param number
	 *            a number in the range of 0-9
	 * @param color
	 *            any color except for {@link UnoCardColor#WILD}
	 *
	 * @throws IllegalArgumentException
	 *             if {@code number} is not in the allowed range or {@code color} is
	 *             equal to {@link UnoCardColor#WILD}
	 */
	public UnoNumericCard(@Nonnull UnoCardColor color, @Nonnegative int number) {
		super(color);

		if (color == UnoCardColor.WILD)
			throw new IllegalArgumentException("The wild card color is not allowed!");

		if (number > 9 || number < 0)
			throw new IllegalArgumentException("Number " + number + " is not in the allowed range (0-9)!");

		this.number = number;
	}

	/**
	 * The number of this card. {@link UnoNumericCard} supports numbers from 0 to 9.
	 *
	 * @return this card's
	 */
	public int getNumber() {
		return this.number;
	}

	@Override
	public String toString() {
		return this.getOriginalColor().toString() + " " + this.number;
	}

	@Override
	public UnoCard cloneCard() {
		return new UnoNumericCard(getColor(), getNumber());
	}

}
