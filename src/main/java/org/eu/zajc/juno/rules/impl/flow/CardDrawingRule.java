package org.eu.zajc.juno.rules.impl.flow;

import static org.eu.zajc.juno.utils.UnoGameUtils.canPlaceCard;
import static org.eu.zajc.juno.utils.UnoRuleUtils.filterRuleKind;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.UnoDrawCard;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.types.UnoGameFlowRule;
import org.eu.zajc.juno.rules.types.flow.*;
import org.eu.zajc.juno.utils.MutableBoolean;

/**
 * The game flow rule responsible for drawing {@link UnoCard}s from the discard pile
 * and adding them to the {@link UnoHand}s when necessary or requested.
 *
 * @author Marko Zajc
 */
public class CardDrawingRule implements UnoGameFlowRule {

	private static final String DRAW_CARDS = "%s draws %s cards from a %s.";
	private static final String DRAW_CARD = "%s draws a card.";

	@Override
	public UnoInitializationConclusion initializationPhase(UnoPlayer player, UnoGame game) {
		if (game.getTopCard() instanceof UnoDrawCard && game.getTopCard().isOpen()) {
			((UnoDrawCard) game.getTopCard()).drawTo(game, player);
			game.onEvent(DRAW_CARDS, player.getName(), ((UnoDrawCard) game.getTopCard()).getAmount(),
						 game.getTopCard().toString());

			return new UnoInitializationConclusion(false, true);
		}

		return UnoInitializationConclusion.NOTHING;
	}

	@Override
	@SuppressWarnings("null")
	public UnoPhaseConclusion decisionPhase(UnoPlayer player, UnoGame game, UnoCard decidedCard) {
		MutableBoolean shouldRepeat = new MutableBoolean();
		if (decidedCard == null) {
			UnoCard drawn = player.getHand().draw(game, 1).get(0);
			game.onEvent(DRAW_CARD, player.getName());

			if (canPlaceCard(player, game, drawn) && player.shouldPlayDrawnCard(game, drawn)) {
				filterRuleKind(game.getRules().getRules(), UnoGameFlowRule.class).forEach(gfr -> {
					UnoPhaseConclusion conclusion = gfr.decisionPhase(player, game, drawn);
					if (conclusion.shouldRepeat())
						shouldRepeat.set(true);
					if (conclusion.shouldReverseDirection())
						game.reverseDirection();
				});
			}
		}

		if (decidedCard instanceof UnoDrawCard && !decidedCard.isOpen())
			decidedCard.markOpen();

		return new UnoPhaseConclusion(shouldRepeat.get(), false);
	}

}
