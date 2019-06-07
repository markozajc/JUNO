package com.github.markozajc.juno.rules;

import javax.annotation.Nullable;

/**
 * An interface representing rules in UNOy.
 *
 * @param <T>
 *            type of the rule
 * @author Marko Zajc
 */
public interface UnoRule<T extends UnoRule<?>> {

	public enum ConflictResolution {

		FAIL,
		REPLACE,
		BACKOFF

	}

	@Nullable
	public default ConflictResolution conflictsWith(T r) {
		return null;
	}

}
