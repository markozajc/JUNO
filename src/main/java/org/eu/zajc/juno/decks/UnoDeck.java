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
package org.eu.zajc.juno.decks;

import java.util.*;

import javax.annotation.Nonnull;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.piles.UnoPile;

/**
 * A class representing a UNO deck. UNO decks are taken and taken at the beginning of
 * a UNO round and cloned. The clone {@link List} of the {@link UnoCard} is then
 * distributed among the {@link UnoPile}s.
 *
 * @author Marko Zajc
 */
public class UnoDeck {

	/**
	 * Clones a {@link Collection} of cards into a modifiable {@link List}. This uses
	 * {@link UnoCard#cloneCard()} to clone the cards, so (if the {@link UnoCard}
	 * implementations implement that method correctly, which they should) the copies of
	 * the {@link UnoCard} aren't be shallow.
	 *
	 * @param cards
	 *            {@link Collection} of {@link UnoCard} to clone
	 *
	 * @return modifiable {@link List} of cloned {@link UnoCard}s
	 */
	@Nonnull
	private static List<UnoCard> cloneCards(@Nonnull Collection<UnoCard> cards) {
		var result = new ArrayList<UnoCard>(cards.size());
		for (UnoCard card : cards) {
			result.add(card.cloneCard());
		}

		return result;
	}

	@Nonnull
	private final List<UnoCard> cards;

	/**
	 * Creates a new {@link UnoDeck}.
	 *
	 * @param cards
	 *            a {@link List} of {@link UnoCard} this {@link UnoDeck} should consist
	 *            of
	 */
	public UnoDeck(@Nonnull List<UnoCard> cards) {
		this.cards = cards;
	}

	/**
	 * Returns a modifiable clone of the deck.
	 *
	 * @return a clone of the deck
	 */
	@Nonnull
	public List<UnoCard> getCards() {
		return cloneCards(this.cards);
	}

}
