package com.github.markozajc.juno.rules.types.flow;

public class UnoTurnInitializationConclusion extends UnoFlowPhaseConclusion {

	/**
	 * A {@link UnoTurnInitializationConclusion} indicating that nothing of interest has
	 * happened.
	 */
	public static final UnoTurnInitializationConclusion NOTHING = new UnoTurnInitializationConclusion();

	private final boolean shouldLoseATurn;

	public UnoTurnInitializationConclusion() {
		this(false, false);
	}

	public UnoTurnInitializationConclusion(boolean shouldRepeat, boolean shouldLoseATurn) {
		super(shouldRepeat);
		this.shouldLoseATurn = shouldLoseATurn;
	}

	public boolean shouldLoseATurn() {
		return this.shouldLoseATurn;
	}

}
