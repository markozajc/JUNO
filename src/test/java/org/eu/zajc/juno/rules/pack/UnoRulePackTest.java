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
package org.eu.zajc.juno.rules.pack;

import static org.eu.zajc.juno.rules.UnoRule.ConflictResolution.*;

import org.eu.zajc.juno.rules.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnoRulePackTest {

	public static class TestRule implements UnoRule {}

	public static class TestRuleReplace implements UnoRule {

		@Override
		public ConflictResolution conflictsWith(UnoRule rule) {
			if (rule instanceof TestRule)
				return REPLACE;

			return null;
		}

	}

	public static class TestRuleBackoff implements UnoRule {

		@Override
		public ConflictResolution conflictsWith(UnoRule rule) {
			if (rule instanceof TestRule)
				return BACKOFF;

			return null;
		}

	}

	public static class TestRuleFail implements UnoRule {

		@Override
		public ConflictResolution conflictsWith(UnoRule rule) {
			if (rule instanceof TestRule)
				return FAIL;

			return null;
		}

	}

	@Test
	void testResolveConflictsNothing() throws UnoRuleConflictException {
		UnoRulePack packNothing = new UnoRulePack(new TestRule());
		// Creates a new rulepack without conflicting rules

		UnoRulePack packNothingResolved = packNothing.resolveConflicts();
		// Resolves (nonexistent) conflicts

		assertEquals(TestRule.class, packNothingResolved.getRules().get(0).getClass());
		assertEquals(1, packNothingResolved.getRules().size());
		// Tests the situation
	}

	@Test
	void testResolveConflictsReplace() throws UnoRuleConflictException {
		UnoRulePack packReplace = new UnoRulePack(new TestRule(), new TestRuleReplace());
		// Creates a new rulepack with conflicting rules (ConflictResolution.REPLACE)

		UnoRulePack packReplaceResolved = packReplace.resolveConflicts();
		// Resolves conflicts

		assertEquals(TestRuleReplace.class, packReplaceResolved.getRules().get(0).getClass());
		assertEquals(1, packReplaceResolved.getRules().size());
		// Tests the situation
	}

	@Test
	void testResolveConflictsBackoff() throws UnoRuleConflictException {
		UnoRulePack packBackoff = new UnoRulePack(new TestRule(), new TestRuleBackoff());
		// Creates a new rulepack with conflicting rules (ConflictResolution.BACKOFF)

		UnoRulePack packBackoffResolved = packBackoff.resolveConflicts();
		// Resolves conflicts

		assertEquals(TestRule.class, packBackoffResolved.getRules().get(0).getClass());
		assertEquals(1, packBackoffResolved.getRules().size());
		// Tests the situation
	}

	@Test
	void testResolveConflictsFail() {
		UnoRulePack packFail = new UnoRulePack(new TestRule(), new TestRuleFail());
		// Creates a new rulepack with conflicting rules (ConflictResolution.FAIL)

		assertThrows(UnoRuleConflictException.class, () -> packFail.resolveConflicts());
	}
}
