package com.github.markozajc.juno.game;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.pack.UnoRulePack;

public class UnoControlledGame extends UnoGame {

	public UnoControlledGame(@Nonnull UnoHand playerOneHand, @Nonnull UnoHand playerTwoHand, @Nonnull UnoDeck unoDeck,
			@Nonnegative int cardAmount, @Nonnull UnoRulePack rules) {
		super(playerOneHand, playerTwoHand, unoDeck, cardAmount, rules);
	}

	@Override
	protected void playHand(UnoHand hand) {
		// TODO actually add stuff
	}

}
