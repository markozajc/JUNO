package com.github.markozajc.juno.cards.impl;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;

public class UnoActionCard implements UnoCard {

	public enum UnoAction {

		SKIP("skip"),
		REVERSE("reverse");

		private final String text;

		private UnoAction(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return this.text;
		}

	}

	private UnoCardColor color;
	private UnoAction action;

	public UnoActionCard(UnoAction action, UnoCardColor color) {
		this.color = color;
		this.action = action;
	}

	@Override
	public UnoCardColor getColor() {
		return this.color;
	}

	public UnoAction getAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return this.color.toString() + " " + this.action.toString();
	}

	@Override
	public void reset() {
		// Doesn't have a state, nothing to reset
	}

}
