package com.github.markozajc.juno.cards;

/**
 * A class representing a card in UNO. The only mandatory thing for a card is a color, which can be one of five colors in {@link UnoCardColor}.
 *
 * @author Marko Zajc
 */
public interface UnoCard {

	/**
	 * @return this card's color
	 */
	abstract UnoCardColor getColor();

	/**
	 * Resets the card's state. A state can be a custom color
	 *
	 */
	abstract void reset();

	/**
	 * Whether this card has been "played" yet. A played card means that its action has been executed.
	 *
	 * @return this card's state
	 */
	default boolean isPlayed() {
		return true;
	}

}
