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
package org.eu.zajc.juno.piles;

import java.util.List;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.piles.impl.*;

/**
 * A interface representing a pile of cards. Normally a UNO games has two regular
 * piles ({@link UnoDrawPile} and {@link UnoDiscardPile}) and {@link UnoHand}s, which
 * also implement this.
 *
 * @author Marko Zajc
 */
public interface UnoPile {

	/**
	 * @return either a modifiable clone or an unmodifiable list of this pile's cards
	 */
	List<UnoCard> getCards();

	/**
	 * @return this pile's size
	 */
	int getSize();

}
