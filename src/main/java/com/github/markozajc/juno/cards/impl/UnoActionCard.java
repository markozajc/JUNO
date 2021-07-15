package com.github.markozajc.juno.cards.impl;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.*;

/**
 * A card that comes in two variations and changes the flow of the game and comes in
 * two variations:
 * <ul>
 * <li>Reverse reverses the player order
 * <li>Skip skips the next player
 * </ul>
 * In a two-player game both of the cards give the player placing them another turn.
 *
 * @author Marko Zajc
 */
public class UnoActionCard extends UnoCard {

	/**
	 * The two variations of the action card.
	 *
	 * @author Marko Zajc
	 */
	public enum UnoFlowAction {

		/**
		 * Reverse reverses the player order.
		 */
		SKIP("skip"),
		/**
		 * Skip skips the next player.
		 */
		REVERSE("reverse");

		private final String text;

		UnoFlowAction(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return this.text;
		}

	}

	@Nonnull
	private final UnoFlowAction action;

	/**
	 * Creates a new {@link UnoActionCard}.
	 *
	 * @param action
	 *            the {@link UnoFlowAction}
	 * @param color
	 *            the {@link UnoCardColor}
	 */
	public UnoActionCard(@Nonnull UnoCardColor color, @Nonnull UnoFlowAction action) {
		super(color);
		this.action = action;
	}

	/**
	 * @return card's {@link UnoFlowAction}
	 *
	 * @deprecated Use {@link #getFlowAction()} instead
	 */
	@Nonnull
	@Deprecated
	public UnoFlowAction getAction() {
		return getFlowAction();
	}

	/**
	 * @return card's {@link UnoFlowAction}
	 */
	@Nonnull
	public UnoFlowAction getFlowAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return this.getColor().toString() + " " + this.action.toString();
	}

	@Override
	public UnoCard cloneCard() {
		return new UnoActionCard(getColor(), getFlowAction());
	}

}
