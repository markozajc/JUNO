package org.eu.zajc.juno.rules.impl.flow;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.UnoReverseCard;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.types.UnoGameFlowRule;
import org.eu.zajc.juno.rules.types.flow.*;

/**
 * The game flow rule responsible for handling {@link UnoReverseCard}s.
 *
 * @author Marko Zajc
 */
public class ReverseCardRule implements UnoGameFlowRule {

	private static final String FLOW_REVERSED = "The order of play has been switched.";
	private static final String LOST_A_TURN = "%s loses a turn.";

	@Override
	public UnoInitializationConclusion initializationPhase(UnoPlayer player, UnoGame game) {
		if (game.getTopCard() instanceof UnoReverseCard && game.getTopCard().isOpen()) {
			// only occurs in 2-player games
			game.getTopCard().markClosed();
			game.onEvent(LOST_A_TURN, player.getName());
			return new UnoInitializationConclusion(false, true);
		}

		return UnoInitializationConclusion.NOTHING;
	}

	@Override
	public UnoPhaseConclusion decisionPhase(UnoPlayer player, UnoGame game, UnoCard decidedCard) {
		if (decidedCard instanceof UnoReverseCard) {
			if (game.getPlayers().size() > 2) {
				game.onEvent(FLOW_REVERSED);
				return new UnoPhaseConclusion(false, true);

			} else if (!decidedCard.isOpen()) {
				// act as in 2-player games
				decidedCard.markOpen();
			}
		}

		return UnoPhaseConclusion.NOTHING;
	}
}
