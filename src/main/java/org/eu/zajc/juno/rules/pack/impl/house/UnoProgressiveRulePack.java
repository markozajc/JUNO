// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java
 * Copyright (C) 2019-2024 Marko Zajc
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.eu.zajc.juno.rules.pack.impl.house;

import static org.eu.zajc.juno.utils.UnoGameUtils.canPlaceCard;
import static org.eu.zajc.juno.utils.UnoRuleUtils.filterRuleKind;

import java.util.*;

import javax.annotation.Nonnull;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.UnoDrawCard;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.piles.impl.UnoDiscardPile;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.UnoRule;
import org.eu.zajc.juno.rules.impl.flow.CardDrawingRule;
import org.eu.zajc.juno.rules.impl.placement.DrawPlacementRules.OpenDrawCardPlacementRule;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules;
import org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;
import org.eu.zajc.juno.rules.types.UnoGameFlowRule;
import org.eu.zajc.juno.rules.types.flow.*;

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

	private static UnoRulePack rules;

	private static void createPack() {
		rules = new UnoRulePack(new PlacementRule(), new FlowRule());
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
	 * @see #getConsecutive(UnoDiscardPile)
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
	 * A modification of {@link OpenDrawCardPlacementRule} that allows draw cards to be
	 * placed over open draw cards of the same amount.
	 *
	 * @author Marko Zajc
	 */
	public static class PlacementRule extends OpenDrawCardPlacementRule {

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

	static class FlowRule extends CardDrawingRule {

		private static void drawAll(List<UnoDrawCard> cards, @Nonnull UnoGame game, @Nonnull UnoPlayer player) {
			int amount = 0;

			for (UnoDrawCard drawCard : cards) {
				drawCard.markClosed();
				amount += drawCard.getAmount();
			}

			player.getHand().draw(game, amount);
		}

		private static final String DRAW_CARDS = "%s draws %s cards from %s %s%s.";
		private static final String DRAW_CARD = "%s draws a card.";

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

				if (canPlaceCard(player, game, drawn) && player.shouldPlayDrawnCard(game, drawn)) {
					filterRuleKind(game.getRules().getRules(), UnoGameFlowRule.class)
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
	@Nonnull
	@SuppressWarnings("null")
	public static UnoRulePack getPack() {
		if (rules == null)
			createPack();

		return rules;
	}

}
