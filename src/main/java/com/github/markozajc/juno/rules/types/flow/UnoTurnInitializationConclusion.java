package com.github.markozajc.juno.rules.types.flow;

import com.github.markozajc.juno.hands.UnoHand;

/**
 * An extension of {@link UnoFlowPhaseConclusion} that also lets you choose whether
 * the {@link UnoHand} loses a turn or not.
 *
 * @author Marko Zajc
 */
public class UnoTurnInitializationConclusion extends UnoFlowPhaseConclusion {

	/**
	 * Same as using the no-arguments constructor, but as a constant.
	 */
	public static final UnoTurnInitializationConclusion NOTHING = new UnoTurnInitializationConclusion();

	private final boolean shouldLoseATurn;

	/**
	 * Creates a new {@link UnoTurnInitializationConclusion} that doesn't change
	 * anything.
	 */
	public UnoTurnInitializationConclusion() {
		this(false, false);
	}

	/**
	 * Creates a new {@link UnoTurnInitializationConclusion}.
	 *
	 * @param shouldRepeat
	 *            whether the entire phase should be repeated
	 * @param shouldLoseATurn
	 *            whether the {@link UnoHand} loses the turn
	 */
	public UnoTurnInitializationConclusion(boolean shouldRepeat, boolean shouldLoseATurn) {
		super(shouldRepeat);
		this.shouldLoseATurn = shouldLoseATurn;
	}

	/**
	 * @return whether the {@link UnoHand} loses the turn
	 */
	public boolean shouldLoseATurn() {
		return this.shouldLoseATurn;
	}

}
