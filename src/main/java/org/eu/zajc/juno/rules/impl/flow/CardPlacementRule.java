// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java 
 * Copyright (C) 2019-2023 Marko Zajc
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
