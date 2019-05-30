package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.impl.flow.exception.UnoGameFlowException;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;

public class ColorChoosingRule implements UnoGameFlowRule {

	@Override
	public void turnInitialization(UnoHand hand, UnoGame game) throws UnoGameFlowException {
		if (game.getTopCard().getColor().equals(UnoCardColor.WILD) && game.getTopCard().isPlayed())
			game.getTopCard().setColorMask(hand.chooseColor(game));
	}

}
