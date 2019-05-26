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
	 * The red card color.
	 */
	YELLOW("Yellow"),
	/**
	 * The wild card color.
	 */
	WILD("Wild");

	private final String text;

	private UnoCardColor(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return this.text;
	}
}
