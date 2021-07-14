package com.github.markozajc.juno.rules.pack.impl.house;

import java.util.*;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.piles.impl.UnoDiscardPile;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.impl.flow.CardDrawingRule;
import com.github.markozajc.juno.rules.impl.placement.DrawPlacementRules.OpenDrawCardPlacementRule;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.*;
import com.github.markozajc.juno.utils.*;

/**
 * A house {@link UnoRulePack} that implements the official Progressive UNO house
 * rule. The rule adds the mechanic that adds a stacking penalty to the draw cards;
 * each time a player places a {@link UnoDrawCard}, the next player is allowed to
 * place a {@link UnoDrawCard} of the same amount on top of it, stacking its penalty
 * and "defending" self. This goes on for as long as the players keep placing draw
 * cards. This {@link UnoRulePack} is also referenced by
 * {@link UnoHouseRule#PROGRESSIVE}, which is makes it easy to install into
 * {@link UnoOfficialRules}.
 *
 * @author Marko Zajc
 */
public class UnoProgressiveRulePack {

	private UnoProgressiveRulePack() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = new UnoRulePack(new ProgressiveUnoPlacementRule(), new ProgressiveUnoFlowRule());
	}

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
	 *
	 * @return whether the card is still relevant or not
	 *
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

	/**
	 * Returns the {@link List} of consecutive relevant {@link UnoDrawCard}s from a
	 * discard pile, starting from the top card. Will return an empty list if the top
	 * card is closed or is not a {@link UnoDrawCard}.
	 *
	 * @param discard
	 *            {@link UnoDiscardPile} to search through
	 *
	 * @return {@link List} of consecutive cards
	 */
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

	/**
	 * The placement rule for progressive UNO. Allows {@link UnoDrawCard}s with the same
	 * amount to be placed on top of open {@link UnoDrawCard}. Replaces
	 * {@link OpenDrawCardPlacementRule}.
	 *
	 * @author Marko Zajc
	 */
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

	/**
	 * The flow rule for progressive UNO. Stacks penalty of consecutive
	 * {@link UnoDrawCard}.
	 *
	 * @author Marko Zajc
	 */
	public static class ProgressiveUnoFlowRule extends CardDrawingRule {

		private static void drawAll(List<UnoDrawCard> cards, @Nonnull UnoGame game, @Nonnull UnoPlayer player) {
			int amount = 0;

			for (UnoDrawCard drawCard : cards) {
				drawCard.markClosed();
				amount += drawCard.getAmount();
			}

			player.getHand().draw(game, amount);
		}

		private static final String DRAW_CARDS = "%s drew %s cards from %s %s%s.";
		private static final String DRAW_CARD = "%s drew a card.";

		@SuppressWarnings("null")
		private static final void drawDeterminedCards(@Nonnull UnoGame game, @Nonnull UnoPlayer player) {
			List<UnoDrawCard> consecutive = getConsecutive(game.getDiscard());
			if (!consecutive.isEmpty()) {
				// If the top card is a draw card

				drawAll(consecutive, game, player);
				// Draw all cards to the hand

				game.onEvent(DRAW_CARDS, player.getName(), consecutive.size() * consecutive.get(0).getAmount(),
							 consecutive.size(), consecutive.get(0).toString(), consecutive.size() == 1 ? "" : "s");

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

		@Override
		public UnoInitializationConclusion initializationPhase(UnoPlayer player, UnoGame game) {
			return UnoInitializationConclusion.NOTHING;
		}

		@Override
		public UnoPhaseConclusion decisionPhase(UnoPlayer player, UnoGame game, UnoCard decidedCard) {
			if (decidedCard == null) {
				drawDeterminedCards(game, player);
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

	/**
	 * @return the Progressive UNO house {@link UnoRulePack}
	 */
	@SuppressWarnings("null")
	@Nonnull
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

}
