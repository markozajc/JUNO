package com.mz.uno.hands.impl.expreimental;

import java.util.List;
import java.util.Random;

import com.mz.uno.UnoGame;
import com.mz.uno.UnoUtils;
import com.mz.uno.cards.UnoCard;
import com.mz.uno.cards.UnoCardColor;
import com.mz.uno.hands.Hand;

public class PureRandomHand extends Hand {

	private static final Random RANDOM = new Random();

	public PureRandomHand(String name) {
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
