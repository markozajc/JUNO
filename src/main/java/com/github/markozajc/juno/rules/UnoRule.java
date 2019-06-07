package com.github.markozajc.juno.rules;

import javax.annotation.Nullable;

/**
 * An interface representing rules in UNOy.
 *
 * @author Marko Zajc
 */
public interface UnoRule {

	public enum ConflictResolution {

		FAIL,
		REPLACE,
		BACKOFF

	}

	@Nullable
	public default ConflictResolution conflictsWith(UnoRule r) {
		return null;
	}

}
