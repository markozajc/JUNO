package com.github.markozajc.juno.hands.impl.expreimental;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

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

	/**
	 * Creates a new {@link PureRandomUnoHand}.
	 *
	 * @param name
	 *            hand's name
	 */
	public PureRandomUnoHand(@Nonnull String name) {
		super(name);
	}

	@SuppressWarnings("null")
	@Override
	public UnoCard playCard(UnoGame game, boolean drawn) {
		List<UnoCard> possibilities = UnoRuleUtils.combinedPlacementAnalysis(game.discard.getTop(), this.cards,
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

}
