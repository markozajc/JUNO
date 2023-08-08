package org.eu.zajc.juno.rules.impl.flow;

import static org.eu.zajc.juno.rules.types.flow.UnoPhaseConclusion.NOTHING;
import static org.eu.zajc.juno.utils.UnoGameUtils.placeCard;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.types.UnoGameFlowRule;
import org.eu.zajc.juno.rules.types.flow.UnoPhaseConclusion;

/**
 * The game flow rule responsible for placing the {@link UnoCard}s that
 * {@link UnoHand}s decide to place.
 *
 * @author Marko Zajc
 */
public class CardPlacementRule implements UnoGameFlowRule {

	private static final String CARD_PLACED = "%s places a %s.";
	private static final String INVALID_CARD = "%s tries to place an invalid card.";

	@Override
	public UnoPhaseConclusion decisionPhase(UnoPlayer player, UnoGame game, UnoCard decidedCard) {
		if (decidedCard != null) {
			if (!placeCard(game, player, decidedCard)) {
				game.onEvent(INVALID_CARD, player.getName());
				return new UnoPhaseConclusion(true, false);
			}

			game.onEvent(CARD_PLACED, decidedCard.getPlacer().getName(), decidedCard.toString());
		}

		return NOTHING;
	}

}
