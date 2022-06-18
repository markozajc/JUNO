package com.github.markozajc.juno.cards;

/**
 * An enum defining the supported colors for UNO cards
 *
 * @author Marko Zajc
 */
public enum UnoCardColor {

	/**
	 * The red card color.
	 */
	RED("Red"),
	/**
	 * The green card color.
	 */
	GREEN("Green"),
	/**
	 * The blue card color.
	 */
	BLUE("Blue"),
	/**
	 * The yellow card color.
	 */
	YELLOW("Yellow"),
	/**
	 * The wild (any) card color.
	 */
	WILD("Wild");

	private final String text;

	UnoCardColor(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return this.text;
	}
}
