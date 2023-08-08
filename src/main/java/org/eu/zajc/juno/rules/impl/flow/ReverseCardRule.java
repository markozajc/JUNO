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
