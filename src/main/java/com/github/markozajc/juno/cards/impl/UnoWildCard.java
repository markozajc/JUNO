package com.github.markozajc.juno.cards.impl;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;

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

	public void setColor(UnoCardColor color) {
		this.color = color;
	}

	@Override
	public void reset() {
		this.color = UnoCardColor.WILD;
	}

}
