package com.github.markozajc.juno.cards;

public interface UnoCard {

	abstract UnoCardColor getColor();

	abstract void reset();

	default boolean isPlayed() {
		return true;
	}

}
