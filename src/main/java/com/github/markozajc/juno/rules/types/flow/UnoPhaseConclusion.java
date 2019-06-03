package com.github.markozajc.juno.rules.types.flow;

import com.github.markozajc.juno.rules.types.UnoGameFlowRule;

/**
 * A return value class that lets {@link UnoGameFlowRule}s indicate whether or not to
 * repeat the entire phase.
 *
 * @author Marko Zajc
 */
public class UnoPhaseConclusion {

	/**
	 * Same as using the no-arguments constructor, but as a constant.
	 */
	public static final UnoPhaseConclusion NOTHING = new UnoPhaseConclusion();

	private final boolean shouldRepeat;

	/**
	 * Creates a new {@link UnoPhaseConclusion} that doesn't change anything.
	 */
	public UnoPhaseConclusion() {
		this(false);
	}

	/**
	 * Creates a new {@link UnoPhaseConclusion}.
	 *
	 * @param shouldRepeat
	 *            whether the entire phase should be repeated
	 */
	public UnoPhaseConclusion(boolean shouldRepeat) {
		this.shouldRepeat = shouldRepeat;
	}

	/**
	 * @return whether to repeat the entire phase or not
	 */
	public boolean shouldRepeat() {
		return this.shouldRepeat;
	}

}
