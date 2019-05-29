package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;

public class CardDrawingRule implements UnoGameFlowRule {

	@Override
	public void turnInitialization(UnoHand hand, UnoGame game) {
		if (game.getTopCard() instanceof UnoDrawCard && !game.getTopCard().isPlayed())
			hand.draw(game.draw, ((UnoDrawCard) game.getTopCard()).getAmount());
	}

	@Override
	public void afterHandDecision(UnoHand hand, UnoGame game, UnoCard decidedCard) {
		if (decidedCard == null) {
			hand.draw(game.draw, 1);
		}
	}

	// TODO handle draw pile getting empty

}
