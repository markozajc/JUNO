package org.eu.zajc.juno.rules.types.flow;

import org.eu.zajc.juno.hands.UnoHand;

/**
 * An extension of {@link UnoPhaseConclusion} that also lets you choose whether the
 * {@link UnoHand} loses a turn or not.
 *
 * @author Marko Zajc
 */
public class UnoInitializationConclusion extends UnoPhaseConclusion {

	/**
	 * Same as using the no-arguments constructor, but as a constant.
	 */
	public static final UnoInitializationConclusion NOTHING = new UnoInitializationConclusion();

	private final boolean shouldLoseATurn;

	/**
	 * Creates a new {@link UnoInitializationConclusion} that doesn't change anything.
	 */
	public UnoInitializationConclusion() {
		this(false, false);
	}

	/**
	 * Creates a new {@link UnoInitializationConclusion}.
	 *
	 * @param shouldRepeat
	 *            whether the entire phase should be repeated
	 * @param shouldLoseATurn
	 *            whether the {@link UnoHand} loses the turn
	 */
	public UnoInitializationConclusion(boolean shouldRepeat, boolean shouldLoseATurn) {
		super(shouldRepeat, false);
		this.shouldLoseATurn = shouldLoseATurn;
	}

	/**
	 * @return whether the {@link UnoHand} loses the turn
	 */
	public boolean shouldLoseATurn() {
		return this.shouldLoseATurn;
	}

}
