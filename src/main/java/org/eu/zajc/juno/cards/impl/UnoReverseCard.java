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

import javax.annotation.Nonnull;

import org.eu.zajc.juno.cards.*;

/**
 * A card that reverses the flow of the game. In a two-player game, the player is
 * given another turn instead.
 *
 * @author Marko Zajc
 */
public class UnoReverseCard extends UnoCard {

	/**
	 * Creates a new {@link UnoReverseCard}.
	 *
	 * @param color
	 *            the {@link UnoCardColor}
	 */
	public UnoReverseCard(@Nonnull UnoCardColor color) {
		super(color);
	}

	@Override
	public String toString() {
		return this.getColor() + " reverse";
	}

	@Override
	public UnoCard cloneCard() {
		return new UnoReverseCard(getColor());
	}

}
