package com.github.markozajc.juno.cards;

public enum UnoCardColor {

	RED("Red"),
	GREEN("Green"),
	BLUE("Blue"),
	YELLOW("Yellow"),
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
