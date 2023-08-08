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
package org.eu.zajc.juno.cards.impl;

import static org.eu.zajc.juno.cards.UnoCardColor.WILD;

import org.eu.zajc.juno.cards.UnoCard;

/**
 * A card that can be placed onto anything and allows its placer to change it to any
 * of the four other colors (red, green, blue and yellow).
 *
 * @author Marko Zajc
 */
public class UnoWildCard extends UnoCard {

	/**
	 * Creates a new {@link UnoWildCard}.
	 */
	public UnoWildCard() {
		super(WILD);
	}

	@Override
	public String toString() {
		return getOriginalColor().toString();
	}

	@Override
	public UnoCard cloneCard() {
		return new UnoWildCard();
	}

}
