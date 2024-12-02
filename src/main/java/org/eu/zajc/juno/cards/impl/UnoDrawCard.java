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

import javax.annotation.*;

import org.eu.zajc.juno.cards.*;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.players.UnoPlayer;

/**
 * A card that makes the other player draw two or four cards, depending on the
 * specified amount. There are several specifications when it comes to draw cards:
 * <ul>
 * <li>The only allowed amounts are two and four
 * <li>The draw four card is always wild
 * <li>The draw two card can be of any color, except for wild
 * <li>There are 4 draw four cards in a standard UNO deck
 * <li>There are 8 draw two cards in a standard UNO deck, two for each color (except
 * for wild)
 * </ul>
 *
 * @author Marko Zajc
 */
public class UnoDrawCard extends UnoCard {

	private final int amount;

	/**
	 * Creates a new draw two card.
	 *
	 * @param color
	 *            card's color
	 *
	 * @throws IllegalArgumentException
	 *             if {@code color} is equal to {@link UnoCardColor#WILD}
	 */
	public UnoDrawCard(@Nonnull UnoCardColor color) {
		this(color, 2);

		if (color == WILD)
			throw new IllegalArgumentException("The wild card color is not for draw two cards!");
	}

	/**
	 * Creates a new draw four card.
	 */
	public UnoDrawCard() {
		this(WILD, 4);
	}

	private UnoDrawCard(@Nonnull UnoCardColor color, @Nonnegative int amount) {
		super(color);
		this.amount = amount;
	}

	/**
	 * The "draw amount" of this card. {@link UnoDrawCard} supports draw four and draw
	 * two cards so this is either {@code 2} or {@code 4}.
	 *
	 * @return the draw amount of this cards
	 */
	public int getAmount() {
		return this.amount;
	}

	@Override
	public String toString() {
		return getOriginalColor().toString() + " draw " + this.getAmount();
	}

	/**
	 * Draws the set amount of {@link UnoCard}s from the draw pile of the given
	 * {@link UnoGame} and adds them to a {@link UnoHand}. This method is safe as it uses
	 * {@link UnoHand#draw(UnoGame, int)}.
	 *
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @param player
	 *            the owner of the {@link UnoHand} to add the drawn cards to
	 *
	 * @throws IllegalStateException
	 *             in case this card is already marked as closed
	 */
	public void drawTo(@Nonnull UnoGame game, @Nonnull UnoPlayer player) {
		markClosed();
		player.getHand().draw(game, getAmount());
	}

	@Override
	public UnoCard cloneCard() {
		return new UnoDrawCard(getColor(), getAmount());
	}

}
