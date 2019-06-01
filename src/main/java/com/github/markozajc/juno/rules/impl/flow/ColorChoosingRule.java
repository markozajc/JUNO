package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.UnoFlowPhaseConclusion;
import com.github.markozajc.juno.rules.types.flow.UnoTurnInitializationConclusion;

public class ColorChoosingRule implements UnoGameFlowRule {

	private static final String COLOR_CHANGED = "%s set the color to %s.";

	private UnoTurnInitializationConclusion selectColorForCard(UnoCard card, UnoHand hand, UnoGame game) {
		if (card != null && card.getColor().equals(UnoCardColor.WILD) && card.isPlayed()) {
			UnoCardColor color = hand.chooseColor(game);

			if (color.equals(UnoCardColor.WILD)) {
				game.onEvent("%s tried to set an invalid color.", hand.getName());
				return new UnoTurnInitializationConclusion(true, false);
			}

			card.setColorMask(color);
			game.onEvent(COLOR_CHANGED, hand.getName(), color.toString());
		}

		return UnoTurnInitializationConclusion.NOTHING;
	}

	@Override
	public UnoTurnInitializationConclusion turnInitialization(UnoHand hand, UnoGame game) {
		return selectColorForCard(game.getTopCard(), hand, game);
	}

	@Override
	public UnoFlowPhaseConclusion afterHandDecision(UnoHand hand, UnoGame game, UnoCard decidedCard) {
		return selectColorForCard(decidedCard, hand, game);
	}

}
