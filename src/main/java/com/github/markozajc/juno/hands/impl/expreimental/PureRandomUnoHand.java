package com.github.markozajc.juno.hands.impl.expreimental;

import java.util.List;
import java.util.Random;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.utils.UnoUtils;

public class PureRandomUnoHand extends UnoHand {

	private static final Random RANDOM = new Random();

	public PureRandomUnoHand(String name) {
		super(name);
	}

	@Override
	public UnoCard playCard(UnoGame game, boolean drawn) {
		List<UnoCard> possibilities = UnoUtils.analyzePossibleCards(game.discard.getTop(), this.cards);
		if (possibilities.isEmpty())
			return null;
		return possibilities.get(RANDOM.nextInt(possibilities.size()));
	}

	@Override
	public UnoCardColor chooseColor(UnoGame game) {
		return UnoCardColor.values()[RANDOM.nextInt(3)];
	}

}
