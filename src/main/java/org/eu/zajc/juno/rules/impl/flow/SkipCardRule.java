package org.eu.zajc.juno.rules.impl.flow;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.UnoSkipCard;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.types.UnoGameFlowRule;
import org.eu.zajc.juno.rules.types.flow.*;

/**
 * The game flow rule responsible for handling {@link UnoSkipCard}s.
 *
 * @author Marko Zajc
 */
public class SkipCardRule implements UnoGameFlowRule {

	private static final String LOST_A_TURN = "%s loses a turn.";

	@Override
	public UnoInitializationConclusion initializationPhase(UnoPlayer player, UnoGame game) {
		if (game.getTopCard() instanceof UnoSkipCard && game.getTopCard().isOpen()) {
			game.getTopCard().markClosed();
			game.onEvent(LOST_A_TURN, player.getName());
			return new UnoInitializationConclusion(false, true);
		}

		return UnoInitializationConclusion.NOTHING;
	}

	@Override
	public UnoPhaseConclusion decisionPhase(UnoPlayer player, UnoGame game, UnoCard decidedCard) {
		if (decidedCard instanceof UnoSkipCard && !decidedCard.isOpen())
			decidedCard.markOpen();

		return UnoInitializationConclusion.NOTHING;
	}

}
