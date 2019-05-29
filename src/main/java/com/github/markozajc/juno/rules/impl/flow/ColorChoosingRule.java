package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;

public class ColorChoosingRule implements UnoGameFlowRule {

	@Override
	public void turnInitialization(UnoHand hand, UnoGame game) {
		if (game.getTopCard().getColor().equals(UnoCardColor.WILD) && game.getTopCard().isPlayed())
			/* TODO actually change the color */;
	}

}
