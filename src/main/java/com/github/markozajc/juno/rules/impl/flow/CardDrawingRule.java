package com.github.markozajc.juno.rules.impl.flow;

import static com.github.markozajc.juno.utils.UnoGameUtils.canPlaceCard;
import static com.github.markozajc.juno.utils.UnoRuleUtils.filterRuleKind;

import java.util.concurrent.atomic.AtomicBoolean;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.*;

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

	@SuppressWarnings("null")
	@Override
	public UnoPhaseConclusion decisionPhase(UnoPlayer player, UnoGame game, UnoCard decidedCard) {
		AtomicBoolean shouldRepeat = new AtomicBoolean(false);
		if (decidedCard == null) {
			UnoCard drawn = player.getHand().draw(game, 1).get(0);
			game.onEvent(DRAW_CARD, player.getName());

			if (canPlaceCard(player, game, drawn)
				&& player.shouldPlayDrawnCard(game, drawn, game.getNextPlayer(player))) {
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
