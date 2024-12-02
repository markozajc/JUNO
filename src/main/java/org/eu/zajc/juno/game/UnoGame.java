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
package org.eu.zajc.juno.game;

import static java.lang.Math.floorMod;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.eu.zajc.juno.game.UnoWinner.UnoEndReason.*;
import static org.eu.zajc.juno.utils.UnoRuleUtils.findHouseRules;

import java.io.PrintStream;
import java.util.*;
import java.util.random.RandomGenerator;

import javax.annotation.*;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.*;
import org.eu.zajc.juno.decks.UnoDeck;
import org.eu.zajc.juno.game.UnoWinner.UnoEndReason;
import org.eu.zajc.juno.piles.impl.*;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.UnoRule;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;
import org.eu.zajc.juno.rules.types.UnoGameFlowRule;
import org.eu.zajc.juno.utils.UnoRuleUtils;

/**
 * A class representing a game of UNO. {@link UnoGame} is the thing that controls the
 * flow and actions that take place in a round of UNO. A {@link UnoGame} utilizes
 * {@link UnoRule}s to do stuff and an implementation of this that does it is
 * {@link UnoControlledGame}.
 *
 * @author Marko Zajc
 */
public abstract class UnoGame {

	@Nonnull
	private final List<UnoPlayer> players;
	@Nullable
	private UnoPlayer last;
	@Nonnegative
	private final int cardAmount;
	@Nonnull
	private final UnoDiscardPile discard = new UnoDiscardPile();
	@Nonnull
	private final UnoRulePack rules;
	@Nonnull
	private final UnoDeck deck;
	@Nullable
	private UnoCard topCard;
	@Nullable
	private final RandomGenerator random;
	private UnoDrawPile draw;
	private boolean endRequested;
	private boolean reversedDirection;

	/**
	 * Creates a new UNO game.
	 *
	 * @param deck
	 *            the {@link UnoDeck} to use
	 * @param cardAmount
	 *            the amount of cards each player starts with
	 * @param rules
	 *            the {@link UnoRulePack} for this {@link UnoGame}
	 * @param players
	 *            the {@link UnoPlayer}s for this {@link UnoGame}. Must have at least 2
	 *            elements
	 */
	protected UnoGame(@Nonnull UnoDeck deck, @Nonnegative int cardAmount, @Nonnull UnoRulePack rules,
					  @Nonnull UnoPlayer... players) {
		this(deck, cardAmount, rules, null, players);
	}

	/**
	 * Creates a new UNO game.
	 *
	 * @param deck
	 *            the {@link UnoDeck} to use
	 * @param cardAmount
	 *            the amount of cards each player starts with
	 * @param rules
	 *            the {@link UnoRulePack} for this {@link UnoGame}
	 * @param random
	 *            the random number generator used throughout the game, or {@code null}
	 *            to use the default
	 * @param players
	 *            the {@link UnoPlayer}s for this {@link UnoGame}. Must have at least 2
	 *            elements
	 */
	@SuppressWarnings("null")
	protected UnoGame(@Nonnull UnoDeck deck, @Nonnegative int cardAmount, @Nonnull UnoRulePack rules,
					  @Nullable RandomGenerator random, @Nonnull UnoPlayer... players) {
		if (players.length < 2)
			throw new IndexOutOfBoundsException("Need at least two players for a game of UNO!");
		this.deck = deck;
		this.cardAmount = cardAmount;
		this.rules = rules;
		this.random = random;
		this.players = unmodifiableList(asList(players.clone()));
	}

	/**
	 * Creates a new UNO game.
	 *
	 * @param first
	 *            the first {@link UnoPlayer}
	 * @param second
	 *            the second {@link UnoPlayer}
	 * @param deck
	 *            the {@link UnoDeck} to use
	 * @param cardAmount
	 *            the amount of card each player gets initially
	 * @param rules
	 *            the {@link UnoRulePack} for this {@link UnoGame}
	 *
	 * @deprecated Use {@link UnoGame#UnoGame(UnoDeck, int, UnoRulePack, UnoPlayer...)}
	 *             instead
	 */
	@Deprecated(since = "2.3", forRemoval = true)
	protected UnoGame(@Nonnull UnoPlayer first, @Nonnull UnoPlayer second, @Nonnull UnoDeck deck,
					  @Nonnegative int cardAmount, @Nonnull UnoRulePack rules) {
		this(deck, cardAmount, rules, first, second);
	}

	private void init() {
		this.draw = new UnoDrawPile(this.deck, this.random);
		// Creates the draw pile

		this.getDiscard().clear();
		this.players.forEach(player -> player.getHand().clear());
		// Clears every other pile

		this.getDiscard().add(this.draw.drawInitalCard());
		// Draws the initial card

		this.players.forEach(player -> player.getHand().draw(this, this.cardAmount));
		// Deals the cards
	}

	private void updateTopCard() {
		this.topCard = this.getDiscard().getTop();
	}

	/**
	 * Merges the discard pile into the draw pile. This is to be called by the
	 * implementation when the draw pile gets empty.
	 */
	public void discardIntoDraw() {
		this.getDraw().mergeResetShuffle(this.getDiscard().createDrawPile());
	}

	/**
	 * Gives a {@link UnoPlayer} a turn. This method should get a card to place from a
	 * hand and then place it (if possible).
	 *
	 * @param player
	 *            the {@link UnoPlayer} to give a turn to
	 */
	protected abstract void turn(@Nonnull UnoPlayer player);

	private static boolean checkVictory(UnoPlayer player, UnoDiscardPile discard) {
		return player.getHand().getSize() == 0 && !discard.getTop().isOpen();
	}

	@Nullable
	private UnoPlayer fallbackVictory() {
		Optional<UnoPlayer> winner =
			this.players.stream().min(Comparator.comparingInt(player -> player.getCards().size()));
		if (winner.isPresent() && this.players.stream()
			.filter(player -> player.getCards().size() == winner.get().getCards().size())
			.count() == 1) {
			return winner.get();
		} else {
			return null;
		}
	}

	/**
	 * Plays a game of UNO.
	 *
	 * @return the UnoWinner
	 */
	@Nonnull
	@SuppressWarnings("null")
	public UnoWinner play() {
		init();
		// Initiates game
		onEvent("The game begins!");

		UnoPlayer winnerPlayer = null;

		boolean fallback = false;
		for (UnoPlayer player = this.players.get(0); winnerPlayer == null && !fallback && !this.endRequested; player =
			getNextPlayer(player)) {
			this.last = player;

			winnerPlayer = playAndCheckPlayers(player);
			// Gives the players a turn and checks all players. While only the current player
			// could be checked, certain rules could modify the state of other players, so I'm
			// opting to check all of them instead.

			if (this.getDiscard().getSize() <= 1 && this.getDraw().getSize() == 0) {
				winnerPlayer = fallbackVictory();
				fallback = true;
			}
			// Fallback method used in the case of both piles getting empty. Do note that the
			// game can not continue at this point so a winner
			// must be chosen.
		}
		// Iterates over all players until a winner is declared

		UnoEndReason reason = determineEndReason(winnerPlayer, fallback);
		UnoWinner winner = new UnoWinner(winnerPlayer, reason);
		checkWinnerObjections(winner);

		return winner;
	}

	@Nonnull
	private UnoEndReason determineEndReason(@Nullable UnoPlayer winnerPlayer, boolean fallback) {
		if (fallback)
			return FALLBACK;
		else if (winnerPlayer != null)
			return VICTORY;
		else if (this.endRequested)
			return REQUESTED;
		else
			return UNKNOWN;
	}

	@Nullable
	private UnoPlayer playAndCheckPlayers(@Nonnull UnoPlayer player) {
		updateTopCard();
		// Updates the top card

		turn(player);
		// Plays player's hand

		updateTopCard();
		// Update top card to avoid last card not being cached

		for (UnoPlayer otherPlayer : this.players) {
			if (checkVictory(otherPlayer, this.getDiscard()))
				return otherPlayer;
		}
		// Checks whether any player has won

		return null;
	}

	private void checkWinnerObjections(@Nonnull UnoWinner winner) {
		UnoPlayer newWinner = null;
		boolean winnerObjected = false;
		boolean objectionsConflict = false;
		for (UnoRule rule : this.rules.getRules()) {
			if (rule instanceof UnoGameFlowRule) {
				var result = ((UnoGameFlowRule) rule).finishPhase(winner, this);
				if (result.doesObjectWinner()) {
					if (winnerObjected && newWinner == result.getNewWinner()) {
						objectionsConflict = true;
						break;
					} else {
						winnerObjected = true;
						newWinner = result.getNewWinner();
					}
				}
			}
		}

		if (objectionsConflict) {
			winner.setNewWinner(null);
		} else if (winnerObjected) {
			winner.setNewWinner(newWinner);
		}
	}

	/**
	 * @return the {@link UnoRulePack} in use by this {@link UnoGame}
	 */
	@Nonnull
	public UnoRulePack getRules() {
		return this.rules;
	}

	/**
	 * The top {@link UnoCard} of the discard pile. This is the preferred method of
	 * obtaining the top {@link UnoCard} as it fetches and stores the top card at the end
	 * of each turn, thus only having to actually get it from the {@link UnoDiscardPile}
	 * pile once, reducing response time a little.
	 *
	 * @return the {@link UnoCard} that's on top of the discard pile
	 */
	public UnoCard getTopCard() {
		return this.topCard;
	}

	/**
	 * Called when an event (such as a card being placed, the color of the top card being
	 * changed, etc) occurs. The given format can be formatted with
	 * {@link String#format(String, Object...)} or
	 * {@link PrintStream#printf(String, Object...)} (or a similar method).
	 *
	 * @param format
	 *            the format of the message
	 * @param arguments
	 *            arguments for the format
	 */
	public abstract void onEvent(@Nonnull String format, @Nonnull Object... arguments);

	/**
	 * Returns the draw pile. This is where cards are drawn from by hands. The discard
	 * pile is shuffled and merged into this when this gets empty.
	 *
	 * @return the {@link UnoDrawPile}
	 */
	@Nonnull
	public UnoDrawPile getDraw() {
		UnoDrawPile drawPile = this.draw;
		if (drawPile == null)
			throw new IllegalStateException("The draw pile is null - please play at least one round to initialize the piles.");

		return drawPile;
	}

	/**
	 * Returns the discard pile. This is where {@link UnoPlayer}s place their cards.
	 *
	 * @return the {@link UnoDiscardPile}
	 */
	@Nonnull
	public UnoDiscardPile getDiscard() {
		return this.discard;
	}

	/**
	 * Returns the deck in use. The deck is only used during the draw pile initialization
	 * and always remains the same.
	 *
	 * @return the {@link UnoDeck}
	 */
	@Nonnull
	public UnoDeck getDeck() {
		return this.deck;
	}

	/**
	 * Returns the {@link UnoPlayer} to get the turn after the provided {@link UnoPlayer}
	 * assuming a {@link UnoReverseCard} is not played (in which case
	 * {@link #getPreviousPlayer()} applies).<br>
	 * NOTE: this changes based on the output of {@link #isReversedDirection()}
	 *
	 * @param player
	 *            the relative {@link UnoPlayer}
	 *
	 * @return the {@link UnoPlayer} after {@code player}
	 */
	@Nonnull
	@SuppressWarnings("null")
	public final UnoPlayer getNextPlayer(UnoPlayer player) {
		int playerIndex = getPlayerIndex(player);

		return this.players.get(floorMod(playerIndex + (this.reversedDirection ? -1 : 1), this.players.size()));
	}

	/**
	 * Returns the {@link UnoPlayer} to get the next turn assuming a
	 * {@link UnoReverseCard} is not played (in which case {@link #getPreviousPlayer()}
	 * applies).<br>
	 * NOTE: this changes based on the output of {@link #isReversedDirection()}
	 *
	 * @return the next {@link UnoPlayer}
	 */
	@Nonnull
	public final UnoPlayer getNextPlayer() {
		return getNextPlayer(this.last);
	}

	/**
	 * Returns the {@link UnoPlayer} to get the turn if a {@link UnoReverseCard} is
	 * played by the given {@link UnoPlayer}. This does not return the last player to get
	 * a turn, use {@link #getLastPlayer()} for that.<br>
	 * NOTE: this changes based on the output of {@link #isReversedDirection()}
	 *
	 * @param player
	 *            the relative {@link UnoPlayer}
	 *
	 * @return the {@link UnoPlayer} after {@code player}
	 */
	@Nonnull
	@SuppressWarnings("null")
	public final UnoPlayer getPreviousPlayer(UnoPlayer player) {
		int playerIndex = getPlayerIndex(player);

		return this.players.get(floorMod(playerIndex + (this.reversedDirection ? 1 : -1), this.players.size()));
	}

	/**
	 * Returns the {@link UnoPlayer} to get turn if a {@link UnoReverseCard} is played by
	 * the current {@link UnoPlayer} ({@link #getLastPlayer()}). This does not return the
	 * last player to get a turn, use {@link #getLastPlayer()} for that.<br>
	 * NOTE: this changes based on the output of {@link #isReversedDirection()}
	 *
	 * @return the next {@link UnoPlayer}
	 */
	@Nonnull
	public final UnoPlayer getPreviousPlayer() {
		return getNextPlayer(this.last);
	}

	private int getPlayerIndex(UnoPlayer player) {
		int playerIndex = this.players.indexOf(player);
		if (playerIndex < 0)
			throw new IllegalArgumentException("The provided UnoPlayer is not a part of this UnoGame.");
		return playerIndex;
	}

	/**
	 * @return the first {@link UnoPlayer}. This is the player to get the turn first.
	 *
	 * @deprecated Use {@link UnoGame#getPlayers()} instead
	 */
	@Deprecated(since = "2.3", forRemoval = true)
	@Nonnull
	@SuppressWarnings("null")
	public UnoPlayer getFirstPlayer() {
		return getPlayers().get(0);
	}

	/**
	 * @return the second {@link UnoPlayer}. This is the player to get the turn second.
	 *
	 * @deprecated Use {@link UnoGame#getPlayers()} instead. This method makes the
	 *             now-incorrect assumption that a game of UNO can only have two players!
	 */
	@Deprecated(since = "2.3", forRemoval = true)
	@Nonnull
	@SuppressWarnings("null")
	public UnoPlayer getSecondPlayer() {
		return getPlayers().get(1);
	}

	/**
	 * @return an unmodifiable view of this {@link UnoGame}s {@link UnoPlayer}s.
	 *
	 *         Caution: trying to modify the returned list will result in an
	 *         {@link UnsupportedOperationException}.
	 */
	@Nonnull
	public List<UnoPlayer> getPlayers() {
		return this.players;
	}

	/**
	 * @return the last {@link UnoPlayer} to have played (drawn or placed a card)
	 */
	@Nullable
	public UnoPlayer getLastPlayer() {
		return this.last;
	}

	/**
	 * Fetches the {@link UnoHouseRule}s used in this {@link UnoGame}'s
	 * {@link UnoRulePack} using {@link UnoRuleUtils#findHouseRules(UnoRulePack)}. This
	 * is a singleton so it will consume more resources when called multiple times.
	 *
	 * @return this {@link UnoGame}'s {@link UnoHouseRule}s
	 */
	@Nonnull
	public List<UnoHouseRule> getHouseRules() {
		return findHouseRules(getRules());
	}

	/**
	 * Requests the game to be ended on the next turn. The {@link UnoEndReason} reported
	 * will be {@link UnoEndReason#REQUESTED}.
	 */
	public void endGame() {
		this.endRequested = true;
	}

	/**
	 * @return whether {@link #endGame()} has been called
	 */
	public boolean isEndRequested() {
		return this.endRequested;
	}

	/**
	 * Reverses the direction of this game's current flow. Will cause
	 * {@link UnoGame#getNextPlayer(UnoPlayer)} to return the previous player in the list
	 * instead of the next one.
	 *
	 * If only two players are in the game, this will have no effect.
	 */
	public void reverseDirection() {
		this.reversedDirection = !this.reversedDirection;
	}

	/**
	 * @return whether the game flow has been reversed with a {@link UnoReverseCard}.
	 *         This will always be {@code false} in two-player games as
	 *         {@link UnoReverseCard} acts as {@link UnoSkipCard}.
	 */
	public boolean isReversedDirection() {
		return this.reversedDirection;
	}

}
