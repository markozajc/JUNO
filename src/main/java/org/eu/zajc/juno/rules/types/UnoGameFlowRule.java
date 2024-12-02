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
package org.eu.zajc.juno.rules.types;

import javax.annotation.*;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.game.*;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.UnoRule;
import org.eu.zajc.juno.rules.types.flow.*;

/**
 * A rule type that controls the flow of the game in {@link UnoControlledGame}. Flow
 * control comes in two parts (phases):
 * <ul>
 * <li><b>initialization</b> is the phase called at the beginning of a turn. Rules
 * should activate and close their respective {@link UnoCard} in this phase. This
 * phase returns {@link UnoInitializationConclusion}, which allows repeating the
 * phase and skipping the next (decision) phase - also skipping the player's turn.
 * <li><b>decision</b> is the phase called right after the {@link UnoPlayer} decides
 * a card to place. The rules that implement this phase should be careful not to
 * conflict with each other. The goal of this phase is getting the decided
 * {@link UnoCard} onto the discard pile and opening the cards that will be activated
 * in the next turn by the initialization phase. This phase returns a
 * {@link UnoPhaseConclusion}, which allows repeating the phase.
 * </ul>
 *
 * @author Marko Zajc
 */
public interface UnoGameFlowRule extends UnoRule {

	/**
	 * The phase that is called at the beginning of a turn. This is the phase where you
	 * activate and close {@link UnoCard}s.
	 *
	 * @param player
	 *            {@link UnoPlayer} of this turn
	 * @param game
	 *            the ongoing {@link UnoGame}
	 *
	 * @return a {@link UnoInitializationConclusion}
	 */
	@SuppressWarnings("unused")
	default UnoInitializationConclusion initializationPhase(@Nonnull UnoPlayer player, @Nonnull UnoGame game) {
		return UnoInitializationConclusion.NOTHING;
	}

	/**
	 * The phase that is called after the {@link UnoHand} decides a card. This phase is
	 * meant for marking {@link UnoCard}s as open so that they can be activated and
	 * closed in the {@link #initializationPhase(UnoPlayer, UnoGame)} of the next round.
	 * It is generally not a good idea to activate the placed card in this phase as that
	 * might conflict with other rules.
	 *
	 * @param player
	 *            {@link UnoPlayer} of this turn
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @param decidedCard
	 *            the {@link UnoCard} the hand has decided to place (or {@code null} -
	 *            request to draw a card)
	 *
	 * @return a {@link UnoPhaseConclusion}
	 */
	@SuppressWarnings("unused")
	default UnoPhaseConclusion decisionPhase(@Nonnull UnoPlayer player, @Nonnull UnoGame game,
											 @Nullable UnoCard decidedCard) {
		return UnoPhaseConclusion.NOTHING;
	}

	/**
	 * The phase that is called after the game is ended for any reason. This phase is
	 * meant for altering the winner of the game chosen by the default game behaviour.
	 *
	 * @param winner
	 *            the chosen {@link UnoWinner}
	 * @param game
	 *            the ongoing {@link UnoGame}
	 *
	 * @return a {@link UnoFinishConclusion}
	 */
	@SuppressWarnings("unused")
	default UnoFinishConclusion finishPhase(@Nonnull UnoWinner winner, @Nonnull UnoGame game) {
		return UnoFinishConclusion.NOTHING;
	}

}
