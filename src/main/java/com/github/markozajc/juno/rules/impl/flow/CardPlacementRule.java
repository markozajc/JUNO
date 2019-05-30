package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.utils.UnoGameUtils;

public class CardPlacementRule implements UnoGameFlowRule {

	@SuppressWarnings("null")
	@Override
	public void afterHandDecision(UnoHand hand, UnoGame game, UnoCard decidedCard) {
		if (decidedCard != null) {
			if (!UnoGameUtils.canPlaceCard(hand, game, decidedCard))
				/* TODO throw an exception */ return;

			game.discard.add(decidedCard);
		}
	}

}
