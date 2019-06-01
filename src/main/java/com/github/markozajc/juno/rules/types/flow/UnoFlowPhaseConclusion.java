package com.github.markozajc.juno.rules.types.flow;

public class UnoFlowPhaseConclusion {

	public static final UnoFlowPhaseConclusion NOTHING = new UnoFlowPhaseConclusion();

	private final boolean shouldRepeat;

	public UnoFlowPhaseConclusion() {
		this(false);
	}

	public UnoFlowPhaseConclusion(boolean shouldRepeat) {
		this.shouldRepeat = shouldRepeat;
	}

	public boolean shouldRepeat() {
		return this.shouldRepeat;
	}

}
