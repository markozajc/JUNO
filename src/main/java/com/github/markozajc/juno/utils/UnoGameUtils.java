package com.github.markozajc.juno.utils;

import java.util.Arrays;

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

}
