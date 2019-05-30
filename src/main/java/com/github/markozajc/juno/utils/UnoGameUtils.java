package com.github.markozajc.juno.utils;

import java.util.Arrays;
import java.util.List;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;

public class UnoGameUtils {

	private UnoGameUtils() {}

	@SuppressWarnings("null")
	public static boolean canPlaceCard(@Nonnull UnoHand hand, @Nonnull UnoGame game, @Nonnull UnoCard card) {
		return !UnoRuleUtils.combinedPlacementAnalysis(game.getTopCard(), Arrays.asList(card), game.getRules(), hand)
				.isEmpty();
	}

	@Nonnull
	@CheckReturnValue
	public static List<UnoCard> drawCards(@Nonnull UnoGame game, @Nonnegative int amount) throws IllegalStateException {
		if (game.draw.getSize() < amount) {
			if (game.discard.getSize() < amount /* minimum draw requirement */ + 1 /* the top card */)
				throw new IllegalStateException("Not enough cards in draw or discard piles.");

			game.discardIntoDraw();
		}

		return game.draw.draw(amount);
	}

}
