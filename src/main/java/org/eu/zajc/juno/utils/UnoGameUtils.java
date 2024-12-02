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
package org.eu.zajc.juno.utils;

import static java.util.Arrays.asList;
import static org.eu.zajc.juno.utils.UnoRuleUtils.combinedPlacementAnalysis;

import java.util.List;

import javax.annotation.*;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.piles.impl.UnoDrawPile;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.pack.UnoRulePack;

/**
 * A class containing various utilities that utilize or are connected with the
 * {@link UnoGame}.
 *
 * @author Marko Zajc
 */
public class UnoGameUtils {

	private UnoGameUtils() {}

	/**
	 * Checks whether a card can be placed on the top card. {@link UnoRulePack} and top
	 * of the discard pile are sourced from the given game.
	 *
	 * @param placer
	 *            the {@link UnoPlayer} placing the card
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @param card
	 *            the {@link UnoCard} to check
	 *
	 * @return whether the given card can be placed on top of the discard pile
	 */
	@SuppressWarnings("null")
	public static boolean canPlaceCard(@Nonnull UnoPlayer placer, @Nonnull UnoGame game, @Nonnull UnoCard card) {
		return !combinedPlacementAnalysis(game.getTopCard(), asList(card), game.getRules(), placer.getHand()).isEmpty();
	}

	/**
	 * Safely draws an amount of {@link UnoCard} from the piles in the {@link UnoGame}.
	 * The "safely" of it means that it will shuffle and merge the discard pile into the
	 * draw one in case there are not enough cards in the draw pile, <b>which
	 * differentiates it from {@link UnoDrawPile#draw(int)} and makes it the preferred
	 * way of drawing cards</b>.
	 *
	 * @param game
	 *            the {@link UnoGame} to get the discard and the draw piles from
	 * @param amount
	 *            the amount of cards to draw
	 *
	 * @return {@link List} of drawn {@link UnoCard}s
	 *
	 * @throws IllegalStateException
	 *             in case there are not enough cards in neither the draw nor discard
	 *             pile to satisfy the requested amount
	 */
	@Nonnull
	@CheckReturnValue
	public static List<UnoCard> drawCards(@Nonnull UnoGame game, @Nonnegative int amount) {
		if (game.getDraw().getSize() < amount) {
			if (game.getDiscard().getSize() < amount /* minimum draw requirement */ + 1 /* the top card */)
				throw new IllegalStateException("Not enough cards in draw or discard piles.");

			game.discardIntoDraw();
		}

		return game.getDraw().draw(amount);
	}

	/**
	 * Places a card to the {@link UnoGame}'s discard pile. This will also set the card's
	 * placer accordingly.
	 *
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @param player
	 *            the {@link UnoPlayer} placing the card
	 * @param card
	 *            the {@link UnoCard} to place
	 *
	 * @return whether the card was placed or not. Returns {@code false} in case
	 *         {@link #canPlaceCard(UnoPlayer, UnoGame, UnoCard)} returns {@code false}
	 */
	public static final boolean placeCard(@Nonnull UnoGame game, @Nonnull UnoPlayer player, @Nonnull UnoCard card) {
		if (!canPlaceCard(player, game, card))
			return false;

		player.getHand().addToDiscard(game.getDiscard(), card);
		card.setPlacer(player);

		return true;
	}

}
