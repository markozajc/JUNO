package com.github.markozajc.juno.cards.impl;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;

/**
 * A class representing a numeric card in UNO. There are 2 cards for each number from
 * 1 to 9 for each color and 1 zero card for each color in a standard UNO deck. Wild
 * numeric cards do not exist and are thus not supported by this class. Cards with
 * numbers not in the range of 0-9 also don't exist and are not supported.
 *
 * @author Marko Zajc
 */
public class UnoNumericCard implements UnoCard {

	private final int number;
	private final UnoCardColor color;

	/**
	 * Creates a new {@link UnoNumericCard}.
	 *
	 * @param number
	 *            a number in the range of 0-9
	 * @param color
	 *            any color except for {@link UnoCardColor#WILD}
	 * @throws IllegalArgumentException
	 *             if {@code number} is not in the allowed range or {@code color} is
	 *             equal to {@link UnoCardColor#WILD}
	 */
	public UnoNumericCard(int number, UnoCardColor color) {
		if (color.equals(UnoCardColor.WILD))
			throw new IllegalArgumentException("The wild card color is not allowed!");

		if(number > 9 || number < 0)
			throw new IllegalArgumentException("Number " + number + " is not in the allowed range (0-9)!");

		this.number = number;
		this.color = color;
	}

	@Override
	public UnoCardColor getColor() {
		return this.color;
	}

	public int getNumber() {
		return this.number;
	}

	@Override
	public String toString() {
		return this.color.toString() + " " + this.number;
	}

	@Override
	public void reset() {
		// Doesn't have a state, nothing to reset
	}

}
