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
package org.eu.zajc.juno.cards;

/**
 * An enum defining the supported colors for UNO cards
 *
 * @author Marko Zajc
 */
public enum UnoCardColor {

	/**
	 * The red card color.
	 */
	RED("Red"),
	/**
	 * The green card color.
	 */
	GREEN("Green"),
	/**
	 * The blue card color.
	 */
	BLUE("Blue"),
	/**
	 * The yellow card color.
	 */
	YELLOW("Yellow"),
	/**
	 * The wild (any) card color.
	 */
	WILD("Wild");

	private final String text;

	UnoCardColor(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return this.text;
	}
}
