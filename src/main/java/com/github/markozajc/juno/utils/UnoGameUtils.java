package com.github.markozajc.juno.utils;

import java.util.Arrays;
import java.util.List;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.piles.impl.UnoDrawPile;
import com.github.markozajc.juno.rules.pack.UnoRulePack;

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
	 * @param hand
	 *            the {@link UnoHand} placing the card
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @param card
	 *            the {@link UnoCard} to check
	 * @return whether the given card can be placed on top of the discard pile
	 */
	@SuppressWarnings("null")
	public static boolean canPlaceCard(@Nonnull UnoHand hand, @Nonnull UnoGame game, @Nonnull UnoCard card) {
		return !UnoRuleUtils.combinedPlacementAnalysis(game.getTopCard(), Arrays.asList(card), game.getRules(), hand)
				.isEmpty();
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
	 * @return {@link List} of drawn {@link UnoCard}s
	 * @throws IllegalStateException
	 *             in case there are not enough cards in neither the draw nor discard
	 *             pile to satisfy the requested amount
	 */
	@Nonnull
	@CheckReturnValue
	public static List<UnoCard> drawCards(@Nonnull UnoGame game, @Nonnegative int amount) {
		if (game.draw.getSize() < amount) {
			if (game.discard.getSize() < amount /* minimum draw requirement */ + 1 /* the top card */)
				throw new IllegalStateException("Not enough cards in draw or discard piles.");

			game.discardIntoDraw();
		}

		return game.draw.draw(amount);
	}

}
