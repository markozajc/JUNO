// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java 
 * Copyright (C) 2019-2024 Marko Zajc
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

import org.eu.zajc.juno.rules.UnoRule.ConflictResolution;

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
