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

import static org.eu.zajc.juno.game.UnoWinner.UnoEndReason.REQUESTED;
import static org.eu.zajc.juno.rules.types.flow.UnoFinishConclusion.NOTHING;

import javax.annotation.Nonnull;

import org.eu.zajc.juno.game.*;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.types.UnoGameFlowRule;
import org.eu.zajc.juno.rules.types.flow.UnoFinishConclusion;

/**
 * By default calling {@link UnoGame#endGame()} will result in a draw. Apply this
 * rule pack if you want the other player to win in such case.<br>
 * <b>NOTE: this only works in games with two players. It will do nothing
 * otherwise.</b>
 *
 * @author Marko Zajc
 *
 */
public class UnoFoeWinsOnEndRequestPack {

	private UnoFoeWinsOnEndRequestPack() {}

	private static UnoRulePack rules;

	private static void createPack() {
		rules = new UnoRulePack(new FlowRule());
	}

	static class FlowRule implements UnoGameFlowRule {

		@Override
		public UnoFinishConclusion finishPhase(UnoWinner winner, UnoGame game) {
			UnoPlayer lastPlayer = game.getLastPlayer();
			if (game.getPlayers().size() == 2 && winner.getEndReason() == REQUESTED && lastPlayer != null)
				return new UnoFinishConclusion(game.getNextPlayer());
			else
				return NOTHING;
		}

	}

	/**
	 * @return the "Foe Wins on End Request" house {@link UnoRulePack}
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static UnoRulePack getPack() {
		if (rules == null)
			createPack();

		return rules;
	}

}
