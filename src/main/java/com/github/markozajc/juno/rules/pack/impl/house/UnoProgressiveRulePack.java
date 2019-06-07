package com.github.markozajc.juno.rules.pack.impl.house;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.piles.impl.UnoDiscardPile;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.impl.flow.CardDrawingRule;
import com.github.markozajc.juno.rules.impl.placement.DrawPlacementRules.OpenDrawCardPlacementRule;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.UnoInitializationConclusion;
import com.github.markozajc.juno.rules.types.flow.UnoPhaseConclusion;
import com.github.markozajc.juno.utils.UnoGameUtils;
import com.github.markozajc.juno.utils.UnoRuleUtils;

public class UnoProgressiveRulePack {

	/**
	 * Checks the relevance of the draw card by the following criteria: (relevance is a
	 * part of the progressive UNO rule and determines whether a draw card is included in
	 * the consecutive draw)
	 * <ul>
	 * <li>the card is a draw card
	 * <li>the draw card has the same amount of cards as the previous ones
	 * <li>the draw card is not closed
	 * </ul>
	 *
	 * @param card
	 *            card to check
	 * @param drawMark
	 *            draw amount on the previous draw cards
	 * @return whether the card is still relevant or not
	 * @see #getConsecutiveDraw()
	 */
	private static boolean isRelevant(UnoCard card, int drawMark) {
		return card instanceof UnoDrawCard && ((UnoDrawCard) card).getAmount() == drawMark && card.isOpen();
	}

	/**
	 * Gets the draw mark of this discard pile. Draw mark is a part of the progressive
	 * UNO rule and determines what cards down the discard pile are relevant to the
	 * consecutive draw. This will return {@code 0} if the top card is not a
	 * {@link UnoDrawCard}.
	 *
	 * @return the draw mark or {@code 0}
	 */
	private static int getDrawMark(UnoDiscardPile discard) {
		UnoCard top = discard.getTop();
		if (top instanceof UnoDrawCard) {
			return ((UnoDrawCard) top).getAmount();
		}

		return 0;
	}

	public static List<UnoDrawCard> getConsecutive(UnoDiscardPile discard) {
		int drawMark = getDrawMark(discard);
		if (drawMark == 0)
			return Collections.emptyList();
		// The top card is not a draw card; there's no draw consecutive draw to calculate

		List<UnoDrawCard> consecutive = new ArrayList<>();

		for (UnoCard card : discard.getCards()) {
			if (isRelevant(card, drawMark)) {
				consecutive.add((UnoDrawCard) card);

			} else {
				break;
			}
		}
		// Iterates over the draw pile, until it hits an irrelevant card, adding to the
		// consecutive draw on each relevant one

		return consecutive;
	}

	public static class ProgressiveUnoPlacementRule extends OpenDrawCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (target instanceof UnoDrawCard && target.isOpen()) {
				if (card instanceof UnoDrawCard
						&& ((UnoDrawCard) card).getAmount() == ((UnoDrawCard) target).getAmount())
					return PlacementClearance.ALLOWED;

				return PlacementClearance.PROHIBITED;
			}

			return PlacementClearance.NEUTRAL;
		}

		@Override
		public ConflictResolution conflictsWith(UnoRule rule) {
			if (rule instanceof OpenDrawCardPlacementRule)
				return ConflictResolution.REPLACE;

			return null;
		}

	}

	public static class ProgressiveUnoFlowRule extends CardDrawingRule {

		private static final String DRAW_CARDS = "%s drew %s cards from %s %ss.";
		private static final String DRAW_CARD = "%s drew a card.";

		@Override
		public UnoInitializationConclusion initializationPhase(UnoPlayer player, UnoGame game) {
			return UnoInitializationConclusion.NOTHING;
		}

		@SuppressWarnings("null")
		@Override
		public UnoPhaseConclusion decisionPhase(UnoPlayer player, UnoGame game, UnoCard decidedCard) {
			if (decidedCard == null) {
				List<UnoDrawCard> consecutive = getConsecutive(game.getDiscard());
				if (!consecutive.isEmpty()) {
					// If the top card is a draw card

					for (UnoDrawCard drawCard : consecutive)
						drawCard.drawTo(game, player);
					// Draw all cards to the hand

					game.onEvent(DRAW_CARDS, player.getName(), consecutive.size() * consecutive.get(0).getAmount(),
						consecutive.size(), consecutive.get(0).toString());

				} else {
					// If the top card is not a draw card

					UnoCard drawn = player.getHand().draw(game, 1).get(0);
					game.onEvent(DRAW_CARD, player.getName());
					// Draw a single card to the hand

					if (UnoGameUtils.canPlaceCard(player, game, drawn)
							&& player.shouldPlayDrawnCard(game, drawn, game.nextPlayer(player))) {
						UnoRuleUtils.filterRuleKind(game.getRules().getRules(), UnoGameFlowRule.class)
								.forEach(gfr -> gfr.decisionPhase(player, game, drawn));
					}
					// Allow the player to place the card (if possible)
				}
			}

			if (decidedCard instanceof UnoDrawCard && !decidedCard.isOpen())
				decidedCard.markOpen();

			return UnoPhaseConclusion.NOTHING;
		}

		@Override
		public ConflictResolution conflictsWith(UnoRule rule) {
			if (rule instanceof CardDrawingRule)
				return ConflictResolution.REPLACE;

			return null;
		}

	}

}
