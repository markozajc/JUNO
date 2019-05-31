package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.impl.flow.exception.UnoGameFlowException;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;

public class ColorChoosingRule implements UnoGameFlowRule {

	private void selectColorForCard(UnoCard card, UnoHand hand, UnoGame game) {
		if (card != null && card.getColor().equals(UnoCardColor.WILD) && card.isPlayed())
			card.setColorMask(hand.chooseColor(game));
	}



	@Override
	public void turnInitialization(UnoHand hand, UnoGame game) throws UnoGameFlowException {
		selectColorForCard(game.getTopCard(), hand, game);
	}



	@Override
	public void afterHandDecision(UnoHand hand, UnoGame game, UnoCard decidedCard) throws UnoGameFlowException {
		selectColorForCard(decidedCard, hand, game);
	}

}
