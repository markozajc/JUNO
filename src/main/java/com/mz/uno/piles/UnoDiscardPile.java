package com.mz.uno.piles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mz.uno.UnoGame;
import com.mz.uno.cards.UnoCard;
import com.mz.uno.cards.impl.UnoDrawCard;
import com.mz.uno.hands.Hand;

public class UnoDiscardPile implements UnoPile {

	private final List<UnoCard> cards = new ArrayList<>();

	/**
	 * Checks the relevance of the draw card by the following criteria: (relevance is a
	 * part of the progressive UNO rule and determines whether a draw card is included in
	 * the consecutive draw)
	 * <ul>
	 * <li>the card is a draw card
	 * <li>the draw card has the same amount of cards as the previous ones
	 * <li>the draw card is not played already
	 * </ul>
	 *
	 * @param card
	 *            card to check
	 * @param drawMark
	 *            draw amount on the previous draw cards
	 * @return whether the card is still relevant or not
	 * @see #getConsecutiveDraw()
	 */
	private static boolean isRelevant(UnoCard card, int drawMark) {
		return !(card instanceof UnoDrawCard) && ((UnoDrawCard) card).getAmount() == drawMark && !card.isPlayed();
	}

	/**
	 * Gets the draw mark of this discard pile. Draw mark is a part of the progressive
	 * UNO rule and determines what cards down the discard pile are relevant to the
	 * consecutive draw. This will return {@code 0} if the top card is not a
	 * {@link UnoDrawCard}.
	 *
	 * @return the draw mark or {@code 0}
	 */
	private int getDrawMark() {
		UnoCard top = getTop();
		if (top instanceof UnoDrawCard) {
			return ((UnoDrawCard) top).getAmount();
		}

		return 0;
	}

	/**
	 * Calculates the <i>consecutive draw</i> for this discard pile. Consecutive draw is
	 * the main point of the progressive UNO rule (which is adopted by JUNO because it's
	 * really great). It's essentially a count of consecutive "relevant" draw cards
	 * (cards of the same type) from top to bottom, multiplied by the <i>draw mark</i>,
	 * which is used in determining relevance and is the amount
	 * ({@link UnoDrawCard#getAmount()}) of the top card ({@link #getTop()}).<br>
	 * <b>TL;DR</b> consecutive draw is the streak of {@link UnoDrawCard}s.
	 *
	 * @return the consecutive draw or {@code 0} if the top card isn't a
	 *         {@link UnoDrawCard}
	 */
	public int getConsecutiveDraw() {
		int drawMark = getDrawMark();
		if (drawMark == 0)
			return 0;
		// The top card is not a draw card; there's no draw consecutive draw to calculate

		int consecutive = 0;

		for (UnoCard card : this.cards) {
			if (isRelevant(card, drawMark)) {
				consecutive++;
			} else {
				break;
			}
		}
		// Iterates over the draw pile, until it hits an irrelevant card, adding to the
		// consecutive draw on each relevant one

		return consecutive * drawMark;
	}

	/**
	 * Marks the top {@link UnoCard} as played (if possible).
	 */
	public void markTopPlayed() {
		UnoCard top = getTop();
		if (!(top instanceof UnoDrawCard))
			return;

		((UnoDrawCard) top).setPlayed();
	}

	@Override
	public List<UnoCard> getCards() {
		return this.cards;
	}

	@Override
	public int getSize() {
		return this.cards.size();
	}

	/**
	 * Adds a {@link UnoCard} to the pile. This is used when a hand places (discards) a
	 * {@link UnoCard}.
	 *
	 * @param card
	 *            {@link UnoCard} to discard
	 */
	public void add(UnoCard card) {
		this.cards.add(0, card);
	}

	/**
	 * Adds a {@link List} of {@link UnoCard}s to the pile (only used when initializing
	 * the discard pile).
	 *
	 * @param cards
	 *            {@link List} of {@link UnoCard}s to discard
	 */
	public void addAll(List<UnoCard> cards) {
		this.cards.addAll(0, cards);
	}

	/**
	 * Returns the top card. {@link Hand}s must only place cards that are compatible with
	 * this.
	 *
	 * @return the top {@link UnoCard} or {@code null} if this pile is empty (shouldn't
	 *         happen)
	 */
	public UnoCard getTop() {
		if (this.cards.isEmpty())
			return null;

		return this.cards.get(0);
	}

	/**
	 * Creates a {@link UnoDrawPile} from this {@link UnoDiscardPile}. This should
	 * automatically done mid-game by the {@link UnoGame} implementation when the
	 * {@link UnoDrawPile} is all drawn out to refill it. Do note that although this pile
	 * will be emptied when this is called, the top card will remain in order to not
	 * disturb the flow of the game.
	 *
	 * @return a new {@link UnoDrawPile}
	 */
	public UnoDrawPile createDrawPile() {
		if (this.cards.isEmpty())
			return new UnoDrawPile(Collections.emptyList());

		UnoCard top = this.cards.remove(0);

		UnoDrawPile pile = new UnoDrawPile(this.cards);
		this.cards.clear();
		this.cards.add(top);

		return pile;
	}

}
