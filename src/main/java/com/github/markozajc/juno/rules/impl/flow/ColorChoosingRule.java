package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.impl.flow.exception.UnoGameFlowException;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;

public class ColorChoosingRule implements UnoGameFlowRule {

	private static final String COLOR_CHANGED = "%s set the color to %s.";



	private void selectColorForCard(UnoCard card, UnoHand hand, UnoGame game) throws UnoGameFlowException {
		if (card != null && card.getColor().equals(UnoCardColor.WILD) && card.isPlayed()) {
			UnoCardColor color = hand.chooseColor(game);

			if(color.equals(UnoCardColor.WILD)) {
				game.onEvent("%s tried to set an invalid color.", hand.getName());
				throw new UnoGameFlowException(true);
			}

			card.setColorMask(color);
			game.onEvent(COLOR_CHANGED, hand.getName(), color.toString());
		}
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
