package com.github.markozajc.juno.rules;

import com.github.markozajc.juno.rules.UnoRule.ConflictResolution;

/**
 * An {@link Exception} signaling that a rule conflicts with another rule and returns
 * a conflict resolution of {@link ConflictResolution#FAIL}.
 *
 * @author Marko Zajc
 */
public class UnoRuleConflictException extends Exception {

	private final transient UnoRule rule;
	private final transient UnoRule conflicting;

	/**
	 * Creates a new {@link UnoRuleConflictException}
	 *
	 * @param rule
	 *            the {@link UnoRule} we were testing
	 * @param conflicting
	 *            the conflicting {@link UnoRule}
	 */
	public UnoRuleConflictException(UnoRule rule, UnoRule conflicting) {
		this.rule = rule;
		this.conflicting = conflicting;
	}

	@Override
	public String getMessage() {
		return this.rule.getClass().getSimpleName() + " conflicts with " + this.conflicting.getClass().getSimpleName();
	}

	/**
	 * @return the {@link UnoRule} we were testing
	 */
	public UnoRule getRule() {
		return this.rule;
	}

	/**
	 * @return the conflicting {@link UnoRule}
	 */
	public UnoRule getConflicting() {
		return this.conflicting;
	}

}
