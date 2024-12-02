// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java
 * Copyright (C) 2019-2024 Marko Zajc, (Olfi01) Florian Meyer
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
package org.eu.zajc.juno.rules.types.flow;

import org.eu.zajc.juno.rules.types.UnoGameFlowRule;

/**
 * A return value class that lets {@link UnoGameFlowRule}s indicate whether or not to
 * repeat the entire phase.
 *
 * @author Marko Zajc
 */
public class UnoPhaseConclusion {

	/**
	 * Same as using the no-arguments constructor, but as a constant.
	 */
	public static final UnoPhaseConclusion NOTHING = new UnoPhaseConclusion();

	private final boolean shouldRepeat;

	private final boolean shouldReverseDirection;

	/**
	 * Creates a new {@link UnoPhaseConclusion} that doesn't change anything.
	 */
	public UnoPhaseConclusion() {
		this(false, false);
	}

	/**
	 * Creates a new {@link UnoPhaseConclusion}.
	 *
	 * @param shouldRepeat
	 *            whether the entire phase should be repeated
	 * @param shouldReverseDirection
	 *            whether direction should be reversed after this phase
	 */
	public UnoPhaseConclusion(boolean shouldRepeat, boolean shouldReverseDirection) {
		this.shouldRepeat = shouldRepeat;
		this.shouldReverseDirection = shouldReverseDirection;
	}

	/**
	 * @return whether to repeat the entire phase or not
	 */
	public boolean shouldRepeat() {
		return this.shouldRepeat;
	}

	/**
	 * @return whether to reverse the game flow
	 */
	public boolean shouldReverseDirection() {
		return this.shouldReverseDirection;
	}
}
