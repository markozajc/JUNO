package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoReverseCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.*;

public class ReverseCardRule implements UnoGameFlowRule {

	private static final String LOST_A_TURN = "%s loses a turn.";

	@Override
	public UnoInitializationConclusion initializationPhase(UnoPlayer player, UnoGame game) {
		if (game.getPlayers().size() > 2)
			return UnoInitializationConclusion.NOTHING;
		if (game.getTopCard() instanceof UnoReverseCard && game.getTopCard().isOpen()) {
			game.getTopCard().markClosed();
			game.onEvent(LOST_A_TURN, player.getName());
			return new UnoInitializationConclusion(false, true);
		}

		return UnoInitializationConclusion.NOTHING;
	}

	@Override
	public UnoPhaseConclusion decisionPhase(UnoPlayer player, UnoGame game, UnoCard decidedCard) {
		if (decidedCard instanceof UnoReverseCard) {
			if (game.getPlayers().size() > 2)
				return new UnoPhaseConclusion(false, true);
			// otherwise act as skip card
			if (!decidedCard.isOpen())
				decidedCard.markOpen();
		}

		return UnoPhaseConclusion.NOTHING;
	}
}
