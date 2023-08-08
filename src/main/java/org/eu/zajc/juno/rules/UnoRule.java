// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java 
 * Copyright (C) 2019-2023 Marko Zajc
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.eu.zajc.juno.rules;

import javax.annotation.*;

import org.eu.zajc.juno.rules.pack.UnoRulePack;

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
	 *
	 * @return {@link ConflictResolution} or {@code null} if it does not conflict
	 */
	@Nullable
	@SuppressWarnings("unused")
	default ConflictResolution conflictsWith(@Nonnull UnoRule rule) {
		return null;
	}

}
