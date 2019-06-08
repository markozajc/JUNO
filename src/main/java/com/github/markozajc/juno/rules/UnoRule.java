package com.github.markozajc.juno.rules;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.markozajc.juno.rules.pack.UnoRulePack;

/**
 * An interface representing rules in UNOy.
 *
 * @author Marko Zajc
 */
public interface UnoRule {

	/**
	 * Determines how conflicts are handled.
	 *
	 * @author Marko Zajc
	 */
	public enum ConflictResolution {

		/**
		 * Fails (throws a {@link UnoRuleConflictException}) if conflicting.
		 */
		FAIL,

		/**
		 * Replaces the conflicting {@link UnoRule}.
		 */
		REPLACE,

		/**
		 * Backs off and lets the conflicting {@link UnoRule} into the {@link UnoRulePack}.
		 */
		BACKOFF

	}

	/**
	 * Tests whether this rule conflicts with another {@link UnoRule}. Conflicts should
	 * only be one-way, meaning that if rule {@code A} states that it conflicts with rule
	 * {@code B} rule {@code B} shouldn't also state that it conflicts with rule
	 * {@code A}.
	 *
	 * @param rule
	 *            {@link UnoRule} to test
	 * @return {@link ConflictResolution} or {@code null} if it does not conflict
	 */
	@SuppressWarnings("unused")
	@Nullable
	public default ConflictResolution conflictsWith(@Nonnull UnoRule rule) {
		return null;
	}

}
