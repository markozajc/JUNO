package com.mz.uno;

import com.mz.uno.cards.UnoCard;
import com.mz.uno.hands.Hand;
import com.mz.uno.piles.UnoDiscardPile;
import com.mz.uno.piles.UnoDrawPile;
import com.mz.uno.piles.UnoStandardDeck;

public abstract class UnoGame {

	public enum UnoPlayer {
		/**
		 * The first player ({@link Hand})
		 */
		PLAYER1,
		/**
		 * The second player ({@link Hand})
		 */
		PLAYER2,
		/**
		 * No one ({@link UnoGame#playGame()} returns this as a tie)
		 */
		NOBODY
	}

	public final Hand playerOneHand;
	public final Hand playerTwoHand;

	public final UnoDrawPile draw = new UnoDrawPile(UnoStandardDeck.getCards());
	public final UnoDiscardPile discard = new UnoDiscardPile();

	public UnoGame(Hand playerOneHand, Hand playerTwoHand) {
		this.playerOneHand = playerOneHand;
		this.playerTwoHand = playerTwoHand;
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
		this.draw.merge(this.discard.createDrawPile());
	}

	protected abstract void playHand(Hand hand);

	/**
	 * Checks whether a hand has won. Criteria for winning in UNO is
	 * <ul>
	 * <li>Having no more cards ({@link Hand#getSize()} {@code == 0})
	 * <li>The top {@link UnoCard} being played ({@link UnoCard#isPlayed()})
	 * </ul>
	 *
	 * @param hand
	 * @param discard
	 * @return
	 */
	private static boolean checkVictory(Hand hand, UnoDiscardPile discard) {
		return hand.getSize() == 0 && discard.getTop().isPlayed();
	}

	/**
	 * Used as a last resort case when both the {@link UnoDiscardPile} and the
	 * {@link UnoDrawPile} are empty. Either indicates a catastrophic failure in the card
	 * economy (the least likely), that the {@link UnoGame} implementation handled the
	 * two piles improperly (unlikely), that both of the {@link Hand}s malfunctioned
	 * (unlikely) or that both {@link Hand}s were intentionally made to just draw cards
	 * (the most likely).
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

	public UnoPlayer playGame() {
		init();
		// Initiates game

		while (true) {
			if (this.discard.getSize() <= 1 && this.draw.getSize() == 0)
				return fallbackVictory();
			// Fallback method used in the case of both PILES getting empty. Do note that the
			// game can not continue at this point so a winner
			// must be chosen.

			playAndCheckHand(this.playerOneHand, UnoPlayer.PLAYER1, this.playerTwoHand, UnoPlayer.PLAYER2);
			// Plays P1's hand

			playAndCheckHand(this.playerTwoHand, UnoPlayer.PLAYER2, this.playerOneHand, UnoPlayer.PLAYER1);
			// Plays P2's hand
		}
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
	private UnoPlayer playAndCheckHand(Hand playerHand, UnoPlayer player, Hand foeHand, UnoPlayer foe) {
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
