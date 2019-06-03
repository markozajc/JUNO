package com.github.markozajc.juno.hands.impl.expreimental;

import java.util.List;
import java.util.Random;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.utils.UnoRuleUtils;

/**
 * An automated hand that uses {@link Random} to decide everything. Still compiles
 * with the {@link UnoHand} restrictions (doesn't cause BasicUnoGame's illegal
 * methods to be called).
 *
 * @author Marko Zajc
 */
public class PureRandomUnoHand extends UnoHand {

	private static final Random RANDOM = new Random();

	@SuppressWarnings("null")
	@Override
	public UnoCard playCard(UnoGame game) {
		List<UnoCard> possibilities = UnoRuleUtils.combinedPlacementAnalysis(game.getDiscard().getTop(), this.cards,
			game.getRules(), this);
		if (possibilities.isEmpty())
			return null;
		return possibilities.get(RANDOM.nextInt(possibilities.size()));
	}

	@SuppressWarnings("null")
	@Override
	public UnoCardColor chooseColor(UnoGame game) {
		return UnoCardColor.values()[RANDOM.nextInt(3)];
	}

	@Override
	public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard) {
		return RANDOM.nextBoolean();
	}

}
