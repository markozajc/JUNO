package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.UnoFlowPhaseConclusion;
import com.github.markozajc.juno.utils.UnoGameUtils;

public class CardPlacementRule implements UnoGameFlowRule {

	private static final String CARD_PLACED = "%s placed a %s.";
	private static final String INVALID_CARD = "%s tried to place an invalid card.";

	@Override
	public UnoFlowPhaseConclusion afterHandDecision(UnoHand hand, UnoGame game, UnoCard decidedCard) {
		if (decidedCard != null) {
			if (!UnoGameUtils.canPlaceCard(hand, game, decidedCard)) {
				game.onEvent(INVALID_CARD, hand.getName());
				return new UnoFlowPhaseConclusion(true);
			}

			hand.addToDiscard(game.discard, decidedCard);
			game.onEvent(CARD_PLACED, hand.getName(), decidedCard.toString());
		}

		return UnoFlowPhaseConclusion.NOTHING;
	}

}
