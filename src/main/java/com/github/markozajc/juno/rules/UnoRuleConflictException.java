package com.github.markozajc.juno.rules;

public class UnoRuleConflictException extends Exception {

	private final UnoRule rule;
	private final UnoRule conflicting;

	public UnoRuleConflictException(UnoRule rule, UnoRule conflicting) {
		this.rule = rule;
		this.conflicting = conflicting;
	}

	@Override
	public String getMessage() {
		return this.rule.getClass().getSimpleName() + " conflicts with " + this.conflicting.getClass().getSimpleName();
	}

	public UnoRule getRule() {
		return this.rule;
	}

	public UnoRule getConflicting() {
		return this.conflicting;
	}

}
