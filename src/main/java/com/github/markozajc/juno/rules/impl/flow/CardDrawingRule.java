package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.UnoFlowPhaseConclusion;
import com.github.markozajc.juno.rules.types.flow.UnoTurnInitializationConclusion;

public class CardDrawingRule implements UnoGameFlowRule {

	@Override
	public UnoTurnInitializationConclusion turnInitialization(UnoHand hand, UnoGame game) {
		if (game.getTopCard() instanceof UnoDrawCard && !game.getTopCard().isPlayed()) {
			((UnoDrawCard) game.getTopCard()).drawTo(game, hand);
			game.onEvent("%s drew %s cards from a %s.", hand.getName(), ((UnoDrawCard) game.getTopCard()).getAmount(),
				game.getTopCard().toString());

			return new UnoTurnInitializationConclusion(false, true);
		}

		return UnoTurnInitializationConclusion.NOTHING;
	}

	@Override
	public UnoFlowPhaseConclusion afterHandDecision(UnoHand hand, UnoGame game, UnoCard decidedCard) {
		if (decidedCard == null) {
			hand.draw(game, 1);
			game.onEvent("%s drew a card.", hand.getName());
		}

		return UnoFlowPhaseConclusion.NOTHING;
	}

}
