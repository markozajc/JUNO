package com.github.markozajc.juno.rules.impl.flow;

import static com.github.markozajc.juno.rules.types.flow.UnoPhaseConclusion.NOTHING;
import static com.github.markozajc.juno.utils.UnoGameUtils.placeCard;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.UnoPhaseConclusion;

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
				return new UnoPhaseConclusion(true);
			}

			game.onEvent(CARD_PLACED, decidedCard.getPlacer().getName(), decidedCard.toString());
		}

		return NOTHING;
	}

}
