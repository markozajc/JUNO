package com.github.markozajc.juno.rules.types.flow;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;

/**
 * A return value class that lets {@link UnoGameFlowRule}s indicate whether or not to
 * change the winner of a game.
 *
 * @author Marko Zajc
 */
public class UnoFinishConclusion {

	/**
	 * Same as using the no-arguments constructor, but as a constant.
	 */
	public static final UnoFinishConclusion NOTHING = new UnoFinishConclusion();

	private final boolean objectWinner;
	private final UnoPlayer newWinner;

	/**
	 * Creates a new {@link UnoFinishConclusion} that doesn't change anything.
	 */
	public UnoFinishConclusion() {
		this.objectWinner = false;
		this.newWinner = null;
	}

	/**
	 * Creates a new {@link UnoFinishConclusion}.
	 *
	 * @param newWinner
	 *            the new winning {@link UnoPlayer} or {@code null} for a draw
	 */
	public UnoFinishConclusion(@Nonnull UnoPlayer newWinner) {
		this.objectWinner = true;
		this.newWinner = newWinner;

	}

	/**
	 * @return whether to change the winner
	 */
	public boolean doesObjectWinner() {
		return this.objectWinner;
	}

	/**
	 * @return what the new winner should be (irrelevant if {@link #doesObjectWinner()}
	 *         is false)
	 */
	public UnoPlayer getNewWinner() {
		return this.newWinner;
	}

}
