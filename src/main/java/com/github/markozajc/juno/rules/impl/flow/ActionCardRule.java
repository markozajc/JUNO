package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.impl.UnoActionCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.UnoInitializationConclusion;

/**
 * The game flow rule responsible for handling {@link UnoActionCard}s.
 *
 * @author Marko Zajc
 */
public class ActionCardRule implements UnoGameFlowRule {

	private static final String LOST_A_TURN = "%s lost a turn.";

	@Override
	public UnoInitializationConclusion initializationPhase(UnoHand hand, UnoGame game) {
		if (game.getTopCard() instanceof UnoActionCard && !game.getTopCard().isPlayed()) {
			((UnoActionCard) game.getTopCard()).setPlayed();
			game.onEvent(LOST_A_TURN, hand.getName());
			return new UnoInitializationConclusion(false, true);
		}

		return UnoInitializationConclusion.NOTHING;
	}

}
