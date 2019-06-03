package com.github.markozajc.juno.players;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.hands.UnoHand;

public class UnoPlayer {

	@Nonnull
	private UnoHand hand;
	@Nonnull
	private final String name;

	public UnoPlayer(@Nonnull UnoHand hand, @Nonnull String name) {
		this.hand = hand;
		this.name = name;
	}

	@Nonnull
	public UnoHand getHand() {
		return this.hand;
	}

	@Nonnull
	public String getName() {
		return this.name;
	}

	public void setHand(@Nonnull UnoHand hand) {
		this.hand = hand;
	}

}
