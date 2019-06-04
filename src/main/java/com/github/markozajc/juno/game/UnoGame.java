package com.github.markozajc.juno.game;

import java.io.PrintStream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.piles.impl.UnoDiscardPile;
import com.github.markozajc.juno.piles.impl.UnoDrawPile;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.pack.UnoRulePack;

/**
 * A class representing a game of UNO. {@link UnoGame} is the thing that controls the
 * flow and actions that take place in a round of UNO. A {@link UnoGame} utilizes
 * {@link UnoRule}s to do stuff and an implementation of this that does it is
 * {@link UnoControlledGame}.
 *
 * @author Marko Zajc
 */
public abstract class UnoGame {

	/**
	 * The first player.
	 */
	@Nonnull
	private final UnoPlayer first;
	/**
	 * The second player.
	 */
	@Nonnull
	private final UnoPlayer second;

	private final int cardAmount;

	private UnoDrawPile draw;

	@Nonnull
	private final UnoDiscardPile discard = new UnoDiscardPile();

	@Nonnull
	private final UnoRulePack rules;

	@Nonnull
	private final UnoDeck deck;

	private UnoCard topCard;

	/**
	 * Returns the other {@link UnoPlayer}.
	 *
	 * @param player
	 *            the {@link UnoPlayer} to reverse
	 * @return the other {@link UnoPlayer}
	 */
	@Nonnull
	private UnoPlayer reversePlayer(UnoPlayer player) {
		return player.equals(this.first) ? this.second : this.first;
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
	 */
	public UnoGame(@Nonnull UnoPlayer first, @Nonnull UnoPlayer second, @Nonnull UnoDeck deck,
			@Nonnegative int cardAmount, @Nonnull UnoRulePack rules) {
		this.first = first;
		this.second = second;
		this.deck = deck;
		this.cardAmount = cardAmount;
		this.rules = rules;
	}

	private void init() {
		this.draw = new UnoDrawPile(this.deck);
		// Creates the draw pile

		this.discard.clear();
		this.first.getHand().clear();
		this.second.getHand().clear();
		// Clears every other pile

		this.discard.add(this.draw.drawInitalCard());
		// Draws the initial card

		this.first.getHand().draw(this, this.cardAmount);
		this.second.getHand().draw(this, this.cardAmount);
		// Deals the cards
	}

	/**
	 * Updates the {@link UnoCard} returned by {@link #getTopCard()}.
	 */
	private void updateTopCard() {
		this.topCard = this.discard.getTop();
	}

	/**
	 * Merges the discard pile into the draw pile. This is to be called by the
	 * implementation when the draw pile gets empty.
	 */
	public void discardIntoDraw() {
		this.draw.mergeResetShuffle(this.discard.createDrawPile());
	}

	/**
	 * Gives a {@link UnoPlayer} a turn. This method should get a card to place from a
	 * hand and then place it (if possible).
	 *
	 * @param player
	 *            the {@link UnoPlayer} to give a turn to
	 */
	protected abstract void turn(@Nonnull UnoPlayer player);

	/**
	 * Checks whether a {@link UnoPlayer} has won. Criteria for winning in UNO is
	 * <ul>
	 * <li>Having no more cards ({@link UnoHand#getSize()} {@code == 0})
	 * <li>The top {@link UnoCard} being played ({@link UnoCard#isPlayed()})
	 * </ul>
	 *
	 * @param player
	 *            the {@link UnoPlayer} to check
	 * @param discard
	 *            the discard pile
	 * @return
	 */
	private static boolean checkVictory(UnoPlayer player, UnoDiscardPile discard) {
		return player.getHand().getSize() == 0 && discard.getTop().isPlayed();
	}

	/**
	 * Used as a last resort case when both the {@link UnoDiscardPile} and the
	 * {@link UnoDrawPile} are empty. Either indicates a catastrophic failure in the card
	 * economy (the least likely), that the {@link UnoGame} implementation handled the
	 * two piles improperly (unlikely), that both of the {@link UnoPlayer}s'
	 * {@link UnoHand}s malfunctioned (unlikely) or that both {@link UnoPlayer}s'
	 * {@link UnoHand}s were intentionally made to just draw cards (the most likely).
	 *
	 * @return the fallback winner or {@code null} if it's a tie (very, very unlikely,
	 *         but still worth mentioning)
	 */
	@Nullable
	private final UnoPlayer fallbackVictory() {
		if (this.first.getHand().getSize() < this.second.getHand().getSize()) {
			return this.first;
			// P1 has less cards

		} else if (this.second.getHand().getSize() < this.first.getHand().getSize()) {
			return this.second;
			// P2 has less cards
		} else {
			return null;
			// P1 and P2 have the same amount of cards (tie)
		}
	}

	/**
	 * Plays a game of UNO.
	 *
	 * @return the victorious {@link UnoPlayer}
	 */
	@SuppressWarnings("null")
	@Nonnull
	public UnoPlayer playGame() {
		init();
		// Initiates game

		UnoPlayer winner = null;
		UnoPlayer[] players = new UnoPlayer[] {
				this.first, this.second
		};

		for (UnoPlayer player = players[0]; winner == null; player = reversePlayer(player)) {
			UnoPlayer reversePlayer = reversePlayer(player);
			// Gets the other player

			winner = playAndCheckPlayer(player, reversePlayer);
			// Gives the players a turn and checks both

			if (this.discard.getSize() <= 1 && this.draw.getSize() == 0)
				winner = fallbackVictory();
			// Fallback method used in the case of both piles getting empty. Do note that the
			// game can not continue at this point so a winner
			// must be chosen.
		}
		// Iterates over all players until a winner is declared

		return winner;
	}

	/**
	 * Gives a {@link UnoPlayer} a turn and check whether either of the
	 * {@link UnoPlayer}s has won.
	 *
	 * @param player
	 *            the {@link UnoPlayer} to give the turn to
	 * @param foe
	 *            the other {@link UnoPlayer}
	 * @return the victor or {@code null} if nobody has won yet
	 */
	private UnoPlayer playAndCheckPlayer(@Nonnull UnoPlayer player, @Nonnull UnoPlayer foe) {
		updateTopCard();
		// Updates the top card

		turn(player);
		// Plays player's hand

		if (checkVictory(player, this.discard))
			return player;
		// Checks whether whether player has won

		if (checkVictory(foe, this.discard))
			return foe;
		// Checks whether whether the foe has won (only ran if the top card was a draw card
		// and player
		// didn't defend)

		return null;
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
	public abstract void onEvent(String format, Object... arguments);

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
			throw new IllegalStateException(
					"The draw pile is null - please play at least one game to initialize the piles.");

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
	 *
	 * @param player
	 *            the {@link UnoPlayer}
	 * @return the {@link UnoPlayer} after {@code player}
	 */
	protected final UnoPlayer nextPlayer(UnoPlayer player) {
		if (player.equals(this.first)) {
			return this.second;
		} else if (player.equals(this.second)) {
			return this.first;
		} else {
			throw new IllegalArgumentException("The provided UnoPlayer is not a part of this UnoGame.");
		}
	}

}
