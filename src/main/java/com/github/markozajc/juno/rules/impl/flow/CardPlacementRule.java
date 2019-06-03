package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.UnoPhaseConclusion;
import com.github.markozajc.juno.utils.UnoGameUtils;

/**
 * The game flow rule responsible for placing the {@link UnoCard}s that
 * {@link UnoHand}s decide to place.
 *
 * @author Marko Zajc
 */
public class CardPlacementRule implements UnoGameFlowRule {

	private static final String CARD_PLACED = "%s placed a %s.";
	private static final String INVALID_CARD = "%s tried to place an invalid card.";

	@Override
	public UnoPhaseConclusion decisionPhase(UnoHand hand, UnoGame game, UnoCard decidedCard) {
		if (decidedCard != null) {
			if (!UnoGameUtils.placeCard(game, hand, decidedCard)) {
				game.onEvent(INVALID_CARD, hand.getName());
				return new UnoPhaseConclusion(true);
			}

			game.onEvent(CARD_PLACED, decidedCard.getPlacer().getName(), decidedCard.toString());
		}

		return UnoPhaseConclusion.NOTHING;
	}

}
