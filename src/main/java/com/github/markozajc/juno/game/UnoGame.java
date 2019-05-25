package com.github.markozajc.juno.game;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoStandardDeck;
import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.piles.impl.UnoDiscardPile;
import com.github.markozajc.juno.piles.impl.UnoDrawPile;

public abstract class UnoGame {

	public enum UnoPlayer {
		PLAYER1,
		PLAYER2,
		NOBODY;
	}

	@Nonnull
	public final UnoHand playerOneHand;
	@Nonnull
	public final UnoHand playerTwoHand;

	@Nonnull
	public final UnoDrawPile draw;
	@Nonnull
	public final UnoDiscardPile discard = new UnoDiscardPile();

	/**
	 * Returns the other {@link UnoPlayer}.
	 *
	 * @param player
	 *            the {@link UnoPlayer} to reverse
	 * @return the other {@link UnoPlayer}
	 */
	@Nonnull
	private static UnoPlayer reversePlayer(UnoPlayer player) {
		return player.equals(UnoPlayer.PLAYER1) ? UnoPlayer.PLAYER2 : UnoPlayer.PLAYER1;
	}

	/**
	 * Returns hand belonging to the given {@link UnoPlayer}.
	 *
	 * @param player
	 *            the {@link UnoPlayer}
	 * @param playerOneHand
	 *            player one's hand
	 * @param playerTwoHand
	 *            player two's hand
	 * @return {@code player}'s hand
	 */
	@SuppressWarnings("null")
	@Nonnull
	private static UnoHand decideHand(UnoPlayer player, UnoHand playerOneHand, UnoHand playerTwoHand) {
		return player.equals(UnoPlayer.PLAYER1) ? playerOneHand : playerTwoHand;
	}

	public UnoGame(@Nonnull UnoHand playerOneHand, @Nonnull UnoHand playerTwoHand) {
		this(playerOneHand, playerTwoHand, new UnoStandardDeck());
	}

	public UnoGame(@Nonnull UnoHand playerOneHand, @Nonnull UnoHand playerTwoHand, @Nonnull UnoDeck unoDeck) {
		this.playerOneHand = playerOneHand;
		this.playerTwoHand = playerTwoHand;

		this.draw = new UnoDrawPile(unoDeck);
	}

	public void init() {
		this.discard.addAll(this.playerOneHand.clear());
		this.discard.addAll(this.playerTwoHand.clear());

		discardIntoDraw();

		if (this.discard.getTop() == null)
			this.discard.add(this.draw.draw());

		this.playerOneHand.draw(this.draw, 7);
		this.playerTwoHand.draw(this.draw, 7);
	}

	protected void discardIntoDraw() {
		this.draw.mergeResetShuffle(this.discard.createDrawPile());
	}

	protected abstract void playHand(UnoHand hand);

	/**
	 * Checks whether a hand has won. Criteria for winning in UNO is
	 * <ul>
	 * <li>Having no more cards ({@link UnoHand#getSize()} {@code == 0})
	 * <li>The top {@link UnoCard} being played ({@link UnoCard#isPlayed()})
	 * </ul>
	 *
	 * @param hand
	 * @param discard
	 * @return
	 */
	private static boolean checkVictory(UnoHand hand, UnoDiscardPile discard) {
		return hand.getSize() == 0 && discard.getTop().isPlayed();
	}

	/**
	 * Used as a last resort case when both the {@link UnoDiscardPile} and the
	 * {@link UnoDrawPile} are empty. Either indicates a catastrophic failure in the card
	 * economy (the least likely), that the {@link UnoGame} implementation handled the
	 * two piles improperly (unlikely), that both of the {@link UnoHand}s malfunctioned
	 * (unlikely) or that both {@link UnoHand}s were intentionally made to just draw
	 * cards (the most likely).
	 *
	 * @return the fallback winner
	 */
	private final UnoPlayer fallbackVictory() {
		if (this.playerOneHand.getSize() < this.playerTwoHand.getSize()) {
			return UnoPlayer.PLAYER1;
			// P1 has less cards

		} else if (this.playerTwoHand.getSize() < this.playerOneHand.getSize()) {
			return UnoPlayer.PLAYER2;
			// P2 has less cards
		} else {
			return UnoPlayer.NOBODY;
			// P1 and P2 have the same amount of cards (tie)
		}
	}

	/**
	 * Plays a game of UNO.
	 *
	 * @return the winner {@link UnoPlayer}
	 */
	@Nonnull
	public UnoPlayer playGame() {
		init();
		// Initiates game

		UnoPlayer winner = null;
		for (UnoPlayer player = UnoPlayer.PLAYER1; winner == null; player = reversePlayer(player)) {
			UnoPlayer reversePlayer = reversePlayer(player);
			// Gets the other player

			winner = playAndCheckHand(decideHand(player, this.playerOneHand, this.playerTwoHand), player,
				decideHand(reversePlayer, this.playerOneHand, this.playerTwoHand), reversePlayer);
			// Plays the hand and checks both

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
	 * Plays a hand and check whether either of the {@link UnoPlayer}s has won.
	 *
	 * @param playerHand
	 *            player's hand
	 * @param foeHand
	 *            foe's hand
	 * @param player
	 *            the {@link UnoPlayer} that owns the {@code playerHand}
	 * @param foe
	 *            the {@link UnoPlayer} that owns the {@code foeHand}
	 * @return the victor or {@code null} if nobody has won yet
	 */
	private UnoPlayer playAndCheckHand(@Nonnull UnoHand playerHand, @Nonnull UnoPlayer player, @Nonnull UnoHand foeHand, @Nonnull UnoPlayer foe) {
		playHand(playerHand);
		// Plays player's hand

		if (checkVictory(playerHand, this.discard))
			return player;
		// Checks whether whether player has won

		if (checkVictory(foeHand, this.discard))
			return foe;
		// Checks whether whether the foe has won (only ran if the top card was a draw card
		// and player
		// didn't defend)

		return null;
	}
}
